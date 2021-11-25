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
     * Saves a new band into the database
     * @param band The band.
     * @return The band's primary key on success, an exception otherwise
     */
    public long create(Band band);

    /**
     * Updates a band into the database
     * @param band The band.
     * @return The result of the operation
     */
    public boolean update(Band band);

    // TODO: cursor-based pagination
}
