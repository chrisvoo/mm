package services;


import models.stats.Stats;

public interface StatsService {
    /**
     * Return the last record from stats or all the history
     * @return Stats
     */
    Stats getStats(boolean onlyLast);

    /**
     * Upsert of the stats into the database
     * @param stats The stats.
     * @return The stats
     */
    Stats save(Stats stats);
}