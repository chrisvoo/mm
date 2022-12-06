import com.google.inject.Inject;
import routes.*;
import spark.Spark;
import utils.Db;
import utils.EnvVars;
import watcher.Watcher;

import java.io.IOException;

import static spark.Spark.*;

public class Microservice {
    private boolean skipWatcher = false; // used for tests

    @Inject private EnvVars envVars;
    @Inject private Db db;
    @Inject private MusicFilesRoutes musicFilesRoutes;
    @Inject private BandRoutes bandRoutes;
    @Inject private StatsRoutes statsRoutes;
    @Inject private ScannerRoutes scannerRoutes;

    @Inject private InfoRoutes infoRoutes;

    @Inject private Watcher watcher;

    public void start() throws IOException {
        this.start(this.skipWatcher);
    }

    public void start(boolean skipWatcher) throws IOException {
        this.skipWatcher = skipWatcher;

        if (!this.skipWatcher) {
            // 1. watcher service
            watcher.registerAll(envVars.getMusicDirectory()).start();
        }

        // 2. microservice init
        port(envVars.getPort());

        // 3. routes
        musicFilesRoutes.routes();
        bandRoutes.routes();
        scannerRoutes.routes();
        statsRoutes.routes();
        infoRoutes.routes();

        // 4. filters
        after("/*", (request, response) -> response.header(
          "Content-Type",
          "application/json; charset=utf-8"
        ));

        // 5. Error handler routes, as last definition
        ErrorRoutes.routes();

        awaitInitialization();
    }

    /**
     * Close the datasource and shutdown Spark.
     */
    public void stop() {
        db.close();
        if (!this.skipWatcher) {
            watcher.close();
        }

        Spark.stop();
        awaitStop();
    }
}