import com.google.inject.Inject;
import routes.BandRoutes;
import routes.ErrorRoutes;
import routes.MusicFilesRoutes;
import routes.ScannerRoutes;
import spark.Spark;
import utils.Db;
import utils.EnvVars;
import utils.Watcher;

import java.io.IOException;

import static spark.Spark.*;

public class Microservice {
    private boolean skipWatcher = false; // used for tests

    @Inject private EnvVars envVars;
    @Inject private Db db;
    @Inject private MusicFilesRoutes musicFilesRoutes;
    @Inject private BandRoutes bandRoutes;
    @Inject private ScannerRoutes scannerRoutes;
    @Inject private Watcher watcher;

    public void start() throws IOException {
        this.start(this.skipWatcher);
    }

    public void start(boolean skipWatcher) throws IOException {
        this.skipWatcher = skipWatcher;

        if (!this.skipWatcher) {
            // 1. watcher service
            watcher.registerAll(envVars.getMusicDirectory());
        }

        // 2. microservice init
        port(envVars.getPort());

        // routes
        musicFilesRoutes.routes();
        bandRoutes.routes();
        scannerRoutes.routes();

        // Error handler routes, as last definition
        ErrorRoutes.routes();

        awaitInitialization();

        if (!this.skipWatcher) {
            // 3. watch for events (must be last, it's a blocking method!)
            watcher.processEvents();
        }
    }

    /**
     * Close the datasource and shutdown Spark.
     */
    public void stop() {
        db.close();
        if (!this.skipWatcher) {
            watcher.stop();
        }

        Spark.stop();
        awaitStop();
    }
}