package services;


import models.band.Band;
import models.files.MusicFile;

public interface BandService {
    /**
     * Return a band by its primary key
     * @param id Band's primary key
     * @return Band
     */
    public Band getById(long id);

    /**
     * Upsert of a band into the database
     * @param band The band.
     * @return The band
     */
    public Band save(Band band);

    // TODO: cursor-based pagination
}
