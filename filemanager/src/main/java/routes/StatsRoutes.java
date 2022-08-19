package routes;

import com.google.inject.Inject;
import exceptions.ModelException;
import models.Model;
import models.stats.Stats;
import services.StatsService;
import spark.Request;
import spark.Route;
import utils.JsonTransformer;

import java.sql.Timestamp;
import java.time.Instant;

import static spark.Spark.*;

public class StatsRoutes extends routes.Route implements Router {
    private final StatsService statsService;

    @Inject
    public StatsRoutes(StatsService statsService) {
        this.statsService = statsService;
    }

    private Stats getStatsFromRequest(Request req) {
        Stats stats = Model.fromJson(req.body(), Stats.class);
        if (!stats.isValid()) {
            throw new ModelException(stats.getErrors(), stats.getErrorCode());
        }

        stats.setLastUpdate(Timestamp.from(Instant.now()));
        return stats;
    }

    private Route getStats() {
        return (req, res) -> statsService.getStats(true);
    }

    private Route upsert() {
        return (req, res) -> statsService.save(this.getStatsFromRequest(req));
    }

    public void routes() {
        path("/stats", () -> {
            get(
                "",
                "application/json",
                this.getStats(),
                new JsonTransformer()
            );

            post(
                "",
                "application/json",
                this.upsert(),
                new JsonTransformer()
            );
        });
    }
}