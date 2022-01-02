package services;


import models.stats.Stats;

public interface StatsService {
    /**
     * Return the stats (one record only)
     * @return Stats
     */
    Stats getStats();

    /**
     * Upsert of the stats into the database
     * @param stats The stats.
     * @return The stats
     */
    Stats save(Stats stats);
}