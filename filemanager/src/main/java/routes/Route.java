package routes;

import spark.Request;

import java.util.logging.Logger;

public abstract class Route {
    private static final Logger logger = Logger.getLogger(Route.class.getName());

    protected Long getLong(String fieldName, Request req) {
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