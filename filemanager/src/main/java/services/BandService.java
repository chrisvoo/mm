package services;


import models.band.Band;

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

    /**
     * Delete a band.
     * @param id The band's id.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean delete(long id);

    // TODO: cursor-based pagination
}
