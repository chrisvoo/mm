package routes;

import com.google.inject.Inject;
import exceptions.ModelException;
import models.scanner.ScanRequest;
import scanner.Scanner;
import services.ScannerService;
import spark.Request;
import spark.Route;
import utils.JsonTransformer;

import static spark.Spark.path;
import static spark.Spark.post;

public class ScannerRoutes extends routes.Route implements Router {
    private final ScannerService scannerService;
    private Scanner scanner;

    @Inject
    public ScannerRoutes(ScannerService scannerService, Scanner scanner) {
        this.scannerService = scannerService;
        this.scanner = scanner;
    }

    private ScanRequest getScanFromRequest(Request req) {
        ScanRequest scan = ScanRequest.fromJson(req.body());
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