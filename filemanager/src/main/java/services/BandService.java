package services;


import models.band.Band;

public interface BandService {
    /**
     * Return a band by its primary key
     * @param id Band's primary key
     * @return Band
     */
    Band getById(long id);

    /**
     * Upsert of a band into the database
     * @param band The band.
     * @return The band
     */
    Band save(Band band);

    /**
     * Delete a band.
     * @param id The band's id.
     * @return true if the operation was successful, false otherwise.
     */
    boolean delete(long id);

    // TODO: cursor-based pagination
}