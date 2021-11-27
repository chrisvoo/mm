import com.google.inject.Inject;
import routes.BandRoutes;
import routes.ErrorRoutes;
import routes.MusicFilesRoutes;
import services.BandService;
import utils.EnvVars;

import static spark.Spark.*;

public class Microservice {
    @Inject private EnvVars envVars;
    @Inject private MusicFilesRoutes musicFilesRoutes;
    @Inject private BandRoutes bandRoutes;

    public void start() {
        port(envVars.getPort());

        // routes
        musicFilesRoutes.routes();
        bandRoutes.routes();

        // Error handler routes, as last definition
        ErrorRoutes.routes();
    }
}
