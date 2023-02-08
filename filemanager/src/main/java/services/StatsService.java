package services;


import models.scanner.ScanOp;
import models.stats.Stats;

public interface StatsService {
    /**
     * Return the single record from stats
     * @return Stats
     */
    Stats getStats();

    /**
     * Upsert of the stats into the database
     * @param stats The stats.
     * @return The stats
     */
    Stats save(Stats stats);

    public Stats save(ScanOp op);
}