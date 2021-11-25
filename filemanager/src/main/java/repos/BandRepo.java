package repos;

import models.band.Band;
import services.BandService;

public class BandRepo implements BandService {
    /**
     * Return a band by its primary key
     *
     * @param id Band's primary key
     * @return Band
     */
    @Override
    public Band getById(long id) {
        return null;
    }

    /**
     * Saves a new band into the database
     *
     * @param band The band.
     * @return The band's primary key on success, an exception otherwise
     */
    @Override
    public long create(Band band) {
        return 0;
    }

    /**
     * Updates a band into the database
     *
     * @param band The band.
     * @return The result of the operation
     */
    @Override
    public boolean update(Band band) {
        return false;
    }
}
