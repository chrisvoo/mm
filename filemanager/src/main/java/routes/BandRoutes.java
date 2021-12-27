package routes;

import com.google.inject.Inject;
import exceptions.DbException;
import exceptions.ModelException;
import models.Model;
import models.band.Band;
import models.utils.ErrorResponse;
import services.BandService;
import spark.Request;
import spark.Route;
import utils.JsonTransformer;

import static spark.Spark.*;

public class BandRoutes extends routes.Route implements Router {
    private final BandService bandService;

    @Inject
    public BandRoutes(BandService bandService) {
        this.bandService = bandService;
    }

    private Band getBandFromRequest(Request req) {
        Band band = Model.fromJson(req.body(), Band.class);
        if (!band.isValid()) {
            throw new ModelException(band.getErrors(), band.getErrorCode());
        }
        return band;
    }

    private Route getById() {
        return (req, res) -> {
            Long bandId = this.getLongParam("id", req);
            if (bandId == null) {
                return new ErrorResponse("id must be a number", ModelException.INVALID_FIELDS);
            }

            Band band = bandService.getById(bandId);

            if (band == null) {
                return new ErrorResponse(
                    String.format("No band with id %d was found", bandId),
                    DbException.RESOURCE_NOT_FOUND
                );
            }

            return band;
        };
    }

    private Route update() {
        return (req, res) -> {
            Long bandId = this.getLongParam("id", req);
            if (bandId == null) {
                return new ErrorResponse("id must be a number", ModelException.INVALID_FIELDS);
            }

            Band band = this.getBandFromRequest(req);
            band.setId(bandId);
            return bandService.save(band);
        };
    }

    private Route create() {
        return (req, res) -> {
            Band band = this.getBandFromRequest(req);
            if (band.getId() != null) {
                return new ErrorResponse(
                   "When ID is already known, you must call PUT band/:id",
                    ModelException.INVALID_FIELDS
                );
            }
            return bandService.save(band);
        };
    }

    private Route deleteResource() {
        return (req, res) -> {
            Long bandId = this.getLongParam("id", req);
            if (bandId == null) {
                return new ErrorResponse("'id' must be a number", ModelException.INVALID_FIELDS);
            }

            return bandService.delete(bandId);
        };
    }

    public void routes() {
        path("/bands", () -> {
            get(
                "/band/:id",
                "application/json",
                this.getById(),
                new JsonTransformer()
            );

            post(
                "/band",
                "application/json",
                this.create(),
                new JsonTransformer()
            );

            put(
                "/band/:id",
                "application/json",
                this.update(),
                new JsonTransformer()
            );

            delete(
                "/band/:id",
                "application/json",
                this.deleteResource(),
                new JsonTransformer()
            );
        });
    }
}