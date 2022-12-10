package routes;

import exceptions.FileManagerException;
import models.utils.ErrorResponse;
import org.eclipse.jetty.http.HttpStatus;
import routes.utils.JsonTransformer;
import spark.Route;

import static spark.Spark.*;

/**
 * 404/500 handlers
 */
public class ErrorRoutes {
    private static final JsonTransformer jsonTransformer = new JsonTransformer();

    private static final Route notFound = (req, res) -> {
        res.status(HttpStatus.NOT_FOUND_404);
        return jsonTransformer.render(new ErrorResponse("resource not found"));
    };

    private static final Route serverError = (req, res) -> {
        res.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
        return jsonTransformer.render(new ErrorResponse("Ups, something went wrong..."));
    };

    /**
     * Initialize the routes for dealing with 404/500 errors.
     */
    public static void routes() {
        notFound(ErrorRoutes.notFound);
        internalServerError(ErrorRoutes.serverError);
        exception(FileManagerException.class, (exception, request, response) -> {
            response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            response.body(
                jsonTransformer.render(
                    new ErrorResponse(exception.getMessage(), exception.getCode())
                )
            );
        });
    }
}