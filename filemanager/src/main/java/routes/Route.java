package routes;

import spark.Request;

public abstract class Route {
    protected Long getLong(String fieldName, Request req) {
        String field = req.params(fieldName);
        if (field == null) {
            return null;
        }

        try {
            return Long.valueOf(field);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
