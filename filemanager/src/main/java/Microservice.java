import com.google.inject.Inject;
import routes.ErrorRoutes;
import routes.MusicFilesRoutes;
import services.MusicFileService;
import utils.EnvVars;

import static spark.Spark.*;

public class Microservice {
    @Inject private EnvVars envVars;
    @Inject private MusicFileService musicFileService;

    public void start() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            port(envVars.getPort());
            new MusicFilesRoutes(musicFileService).routes();

            // Error handler routes
            ErrorRoutes.routes();
        } catch (ClassNotFoundException e) {
            System.err.println("It seems you're lacking the MySQL Connector driver");
            stop();
        }
    }
}
