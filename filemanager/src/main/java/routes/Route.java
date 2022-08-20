package routes;

import com.google.inject.Inject;
import spark.Request;
import utils.logging.LoggerInterface;

public abstract class Route {
    @Inject
    private LoggerInterface logger;

    protected Long getLongParam(String fieldName, Request req) {
        String field = req.params(fieldName);
        if (field == null) {
            return null;
        }

        try {
            return Long.valueOf(field);
        } catch (NumberFormatException e) {
            logger.severe("getLong: " + e.getMessage());
            return null;
        }
    }
}