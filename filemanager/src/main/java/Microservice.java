import com.google.inject.Inject;
import routes.ErrorRoutes;
import routes.MusicFilesRoutes;
import utils.EnvVars;

import static spark.Spark.*;

public class Microservice {
    @Inject private EnvVars envVars;
    @Inject private MusicFilesRoutes musicFilesRoutes;

    public void start() {
        port(envVars.getPort());

        // routes
        musicFilesRoutes.routes();

        // Error handler routes, as last definition
        ErrorRoutes.routes();
    }
}
