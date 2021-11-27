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
     * Upsert of a band into the database
     *
     * @param band The band.
     * @return The band
     */
    @Override
    public Band save(Band band) {
        return null;
    }
}
