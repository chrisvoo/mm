package models.files;

/**
 * This is part of MusicFiles table. Once it were a table on its own, but it has been downgraded to a simple field, since
 * I'm not sure it's going to be used: it's difficult to retrieve a unique result from MusicBrainz without manually
 * verifying the data.
 */
public class MusicBrainzFile {
    private Long musicFileId;
    private String albumType;
    private String albumStatus;

    /**
     * CHAR(2), country ISO two letters
     */
    private String albumReleaseCountry;

    /**
     * This is a UUID saved as binary in MySQL
     */
    private String workId;

    /**
     * This is a UUID saved as binary in MySQL
     */
    private String albumId;

    /**
     * This is a UUID saved as binary in MySQL
     */
    private String artistId;

    /**
     * This is a UUID saved as binary in MySQL
     */
    private String albumArtistId;

    /**
     * This is a UUID saved as binary in MySQL
     */
    private String releaseGroupId;

    /**
     * This is a UUID saved as binary in MySQL
     */
    private String releaseTrackId;

    /**
     * This is a UUID saved as binary in MySQL
     */
    private String recordingId;

    /**
     * This is a UUID saved as binary in MySQL
     */
    private String acoustidId;

    public Long getMusicFileId() {
        return musicFileId;
    }

    public String getAlbumType() {
        return albumType;
    }

    public MusicBrainzFile setAlbumType(String albumType) {
        this.albumType = albumType;
        return this;
    }

    public String getAlbumStatus() {
        return albumStatus;
    }

    public MusicBrainzFile setAlbumStatus(String albumStatus) {
        this.albumStatus = albumStatus;
        return this;
    }

    public String getAlbumReleaseCountry() {
        return albumReleaseCountry;
    }

    public MusicBrainzFile setAlbumReleaseCountry(String albumReleaseCountry) {
        this.albumReleaseCountry = albumReleaseCountry;
        return this;
    }

    public String getWorkId() {
        return workId;
    }

    public MusicBrainzFile setWorkId(String workId) {
        this.workId = workId;
        return this;
    }

    public String getAlbumId() {
        return albumId;
    }

    public MusicBrainzFile setAlbumId(String albumId) {
        this.albumId = albumId;
        return this;
    }

    public String getArtistId() {
        return artistId;
    }

    public MusicBrainzFile setArtistId(String artistId) {
        this.artistId = artistId;
        return this;
    }

    public String getAlbumArtistId() {
        return albumArtistId;
    }

    public MusicBrainzFile setAlbumArtistId(String albumArtistId) {
        this.albumArtistId = albumArtistId;
        return this;
    }

    public String getReleaseGroupId() {
        return releaseGroupId;
    }

    public MusicBrainzFile setReleaseGroupId(String releaseGroupId) {
        this.releaseGroupId = releaseGroupId;
        return this;
    }

    public String getReleaseTrackId() {
        return releaseTrackId;
    }

    public MusicBrainzFile setReleaseTrackId(String releaseTrackId) {
        this.releaseTrackId = releaseTrackId;
        return this;
    }

    public String getRecordingId() {
        return recordingId;
    }

    public MusicBrainzFile setRecordingId(String recordingId) {
        this.recordingId = recordingId;
        return this;
    }

    public String getAcoustidId() {
        return acoustidId;
    }

    public MusicBrainzFile setAcoustidId(String acoustidId) {
        this.acoustidId = acoustidId;
        return this;
    }
}