import chalk from 'chalk';
import ora from 'ora'
import terminalLink from 'terminal-link';
import { 
    WikiPedia, WikiDiscographyResult, WikiTrackResult, ArtistDetails, 
    Discography, ErrorResponse, MusicBrainz, Track
} from 'discography'
import moment from 'moment';
import Album from './models/Album.js';
import Band from './models/Band.js';
import Song from './models/Song.js';
import BandMusician from './models/BandMusician.js';
import Musician from './models/Musician.js';
import AlbumMusician from './models/AlbumMusician.js';
import ConnectionManager from './ConnectionManager.js';

export default class InfoCatcher {
    private bandName: string;

    private wiki: WikiPedia;
    private brainz: MusicBrainz;

    private band: Band;
    private albums: Album[];
    private musicians: Musician[];

    private dryRun: boolean;

    public constructor(bandName: string, options: { dryRun: boolean }) {
        if (!bandName || bandName.trim().length === 0) {
            console.error(chalk.red('bandName cannot be null'))
            process.exit(1);
        }

        this.dryRun = options.dryRun;
        this.bandName = bandName;

        this.band = new Band()
        this.band.dryRun = this.dryRun

        this.albums = [];
        this.musicians = [];

        this.wiki = new WikiPedia()
        this.brainz = new MusicBrainz()
    }

    /**
     * Tells if a musician has been already saved.
     * @param name Musician's name
     * @returns 
     */
    private musicianExists(name: string): number {
        const musicians = this.musicians.filter((m: Musician): boolean => m.real_name === name);
        if (musicians.length > 0) {
            return musicians[0].id!;
        }

        return -1;
    }

    private getYear(date: string | null | undefined): number | undefined {
        if (!date) {
            return undefined
        }

        if (/^\d{4}-\d{2}-\d{2}$/.test(date)) {
            return parseInt(date.substring(0, 4))
        }

        if (/^\d{4}$/.test(date)) {
            return parseInt(date)
        }

        return undefined;
    }

    public async run(): Promise<void> {
        if (!ConnectionManager.isEnvValid()) {
            console.error(chalk.red('Please provide the .env file with he required variable in the current directory'))
            process.exit(1)
        }

        const spinner = ora({
            text: `Searching ${this.bandName}`,
            stream: process.stdout,
        }).start();

        const response: WikiDiscographyResult = await this.wiki.searchDiscoGraphy(this.bandName)
        if (!response.data) {
            spinner.fail(chalk.red(`Wikipedia failed for ` + this.bandName + `: ${response.message}`));
            process.exit(1);
        }

        const { data: { albums, musicBrainzUrl, pageDetails: { title } } } = response;
        let recordFoundMessage = terminalLink('Wikipedia page found', `https://en.wikipedia.org/wiki/${title}`)
        
        spinner.succeed(recordFoundMessage);
        if (albums.length === 0) {
            spinner.warn(`Found ${albums.length} albums from Wikipedia, trying MusicBrainz!`);
        } else {
            spinner.succeed(`Found ${albums.length} albums`);
        }

        this.band.name = this.bandName
        this.band.musicbrainz_url = musicBrainzUrl
        this.band.total_albums_released = albums.length

        spinner.start('Getting more band\'s info from MusicBrainz')
        
        const resBrainz: Discography | ErrorResponse = await this.brainz.getArtistDiscography(this.bandName, ['Live', 'Compilation', 'Single']);
        
        if ((resBrainz as ErrorResponse).error === true) {
            spinner.fail(chalk.red(`MusicBrainz failed for ` + this.bandName + `: ${(resBrainz as ErrorResponse).message}`))
            process.exit(1);
        } else {
            const brainRes = resBrainz as Discography
            const { country, lifeSpan: { begin, ended }, id, name } = brainRes.artist
            this.band.name = name
            this.band.country = country
            this.band.active_from = this.getYear(begin)
            this.band.active_to = this.getYear(ended)
            this.band.musicbrainz_url = `https://musicbrainz.org/artist/${id}`

            let mex = `Active from ${this.band.active_from}` +
                    (!this.band.active_to ? ', still active' : ` to ${this.band.active_to}`) 
            spinner.succeed(mex)
        }

        await this.band.startTransaction()
    
        try {
            const bandId = await this.band.save()

            if (albums.length === 0) {
                const brainRes = resBrainz as Discography
                for (const releaseGroups of brainRes.releaseGroups) {
                    const { id, title, releaseDate } = releaseGroups

                    const theAlbum = new Album()
                    theAlbum.dryRun = this.dryRun

                    theAlbum.released_on = releaseDate
                    theAlbum.title = title
                    theAlbum.band_id = bandId

                    const albumId = await theAlbum.save()
                    theAlbum.id = albumId
                    this.albums.push(theAlbum)

                    spinner.start(`Searching tracks for ${title}`)
                    const response = await this.brainz.getTracksByReleaseGroup(id)
                    if ((response as ErrorResponse).error) {
                        spinner.fail(chalk.red(`Cannot get tracks for ${theAlbum.title}`))
                        process.exit(1)
                    }

                    const tracks = (response as Track[])
                    let promises: Promise<any>[] = []

                    for (const track of tracks) {
                        const song = new Song()
                        song.dryRun = this.dryRun
    
                        song.album_id = albumId
                        song.title = track.title
    
                        promises.push(song.save()) 
                        await Promise.all(promises)                       
                    }

                    // no musicians so far
                }
            } else {
                for (const item of albums) {
                    const theAlbum = new Album()
                    theAlbum.dryRun = this.dryRun
    
                    const { album, wikiPage } = item
                    theAlbum.released_on = album.released ? moment(album.released, 'D MMMM YYYY').format('YYYY-MM-DD') : undefined
                    theAlbum.title = album.title
                    theAlbum.band_id = bandId
                    theAlbum.wikiPage = wikiPage.replace('/wiki/', '')
    
                    const albumId = await theAlbum.save()
                    theAlbum.id = albumId
                    this.albums.push(theAlbum)
    
                    spinner.start(`Searching tracks for ${album.title}`)            
                    const res: WikiTrackResult = await this.wiki.searchTracks(theAlbum.wikiPage!)
                    if (res.message !== 'OK') {
                        spinner.fail(chalk.red(`Cannot get tracks for ${theAlbum.title}`))
                        throw new Error(res.message)
                    }
    
                    const { tracks, musicians } = res.data!
                    let promises: Promise<any>[] = []
    
                    for (const track of tracks) {
                        const song = new Song()
                        song.dryRun = this.dryRun
    
                        song.album_id = albumId
                        song.title = track.title
    
                        promises.push(song.save())
                    }
    
                    await Promise.all(promises)
                    promises = [];
    
                    for (const musician of musicians) {
                        let musicianId = this.musicianExists(musician.name);
    
                        if (musicianId === -1) {
                            const musicMan = new Musician()
                            musicMan.dryRun = this.dryRun
    
                            musicMan.real_name = musician.name
                            musicMan.instruments = musician.instruments
    
                            const musicManBrainz = await this.brainz.getArtist(musician.name);
                            if ((musicManBrainz as ErrorResponse).error !== true) {
                                const { country, id, lifeSpan: { begin, ended }, name } = (musicManBrainz as ArtistDetails);
                                musicMan.birthday = begin;
                                musicMan.real_name = name;
                                musicMan.died = ended ?? undefined;
                                musicMan.musicbrainz_url = `https://musicbrainz.org/artist/${id}`
                            }
    
                            musicianId = await musicMan.save()
                            musicMan.id = musicianId
                            this.musicians.push(musicMan)
    
                            const bandMusician = new BandMusician()
                            bandMusician.dryRun = this.dryRun
    
                            bandMusician.band_id = bandId
                            bandMusician.musician_id = musicianId
                            promises.push(bandMusician.save())
                        }
    
                        const albumMusicians = new AlbumMusician()
                        albumMusicians.dryRun = this.dryRun
    
                        albumMusicians.album_id = albumId
                        albumMusicians.musician_id = musicianId
                        promises.push(albumMusicians.save())
                    }
    
                    await Promise.all(promises)
                    spinner.succeed(`Album ${theAlbum.title} saved!`)
                }
            }          

            await this.band.commit()
            spinner.succeed('Process finished')
        } catch (e) {
            await this.band.rollback()
            spinner.fail('Procedure rollbacked :-(')
            console.error('\n')
            console.error(e)
        } finally {
            if (!this.dryRun) {
                ConnectionManager.disconnect()
            }
        }
    }
}