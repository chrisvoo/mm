package routes;

import com.google.inject.Inject;
import services.BandService;
import spark.Route;
import utils.JsonTransformer;

import static spark.Spark.*;

public class BandRoutes implements Router {
    private final BandService bandService;

    @Inject
    public BandRoutes(BandService bandService) {
        this.bandService = bandService;
    }

    private Route getById() {
        return (req, res) -> bandService.getById(5);
    }

    private Route save() {
        return (req, res) -> bandService.save(null);
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
                this.save(),
                new JsonTransformer()
            );
        });
    }
}
