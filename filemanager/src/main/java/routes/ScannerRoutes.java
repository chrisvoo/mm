package routes;

import com.google.inject.Inject;
import exceptions.ModelException;
import models.Model;
import models.scanner.ScanRequest;
import scanner.Scanner;
import services.ScannerService;
import spark.Request;
import spark.Route;
import utils.EnvVars;
import utils.JsonTransformer;

import static spark.Spark.path;
import static spark.Spark.post;

public class ScannerRoutes extends routes.Route implements Router {
    private final ScannerService scannerService;
    private Scanner scanner;
    private EnvVars envVars;

    @Inject
    public ScannerRoutes(ScannerService scannerService, Scanner scanner, EnvVars envVars) {
        this.scannerService = scannerService;
        this.scanner = scanner;
        this.envVars = envVars;
    }

    /**
     * If the client doesn't
     * @param req
     * @return
     */
    private ScanRequest getScanFromRequest(Request req) {
        ScanRequest scan = Model.fromJson(req.body(), ScanRequest.class);
        // default directory
        if (scan.getDirectory() == null) {
            scan.setDirectory(envVars.getMusicDirectory());
        }

        if (!scan.isValid()) {
            throw new ModelException(scan.getErrors(), scan.getErrorCode());
        }
        return scan;
    }

    private Route scan() {
        return (req, res) -> {
            ScanRequest scanReq = this.getScanFromRequest(req);
            this.scanner.setTargetDir(scanReq.getDirectory()).start();
            return true;
        };
    }

    public void routes() {
        path("/scanner", () -> {
            post(
              "/scan",
              "application/json",
              this.scan(),
              new JsonTransformer()
            );
        });
    }
}