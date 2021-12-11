package routes;

import com.google.inject.Inject;
import exceptions.DbException;
import exceptions.ModelException;
import models.files.MusicFile;
import models.utils.ErrorResponse;
import services.MusicFileService;
import spark.Request;
import spark.Route;
import utils.JsonTransformer;

import static spark.Spark.*;

public class MusicFilesRoutes extends routes.Route implements Router {
    private final MusicFileService musicFileService;

    @Inject
    public MusicFilesRoutes(MusicFileService musicFileService) {
        this.musicFileService = musicFileService;
    }

    private MusicFile getMusicFileFromRequest(Request req) {
        MusicFile file = MusicFile.fromJson(req.body());
        if (!file.isValid()) {
            throw new ModelException(file.getErrors(), file.getErrorCode());
        }
        return file;
    }

    private Route getById() {
        return (req, res) -> {
            Long fileId = this.getLong("id", req);
            if (fileId == null) {
                return new ErrorResponse("id must be a number", ModelException.INVALID_FIELDS);
            }

            MusicFile file = musicFileService.getById(fileId);

            if (file == null) {
                return new ErrorResponse(
                  String.format("No file with id %d was found", fileId),
                  DbException.RESOURCE_NOT_FOUND
                );
            }

            return file;
        };
    }

    private Route update() {
        return (req, res) -> {
            Long fileId = this.getLong("id", req);
            if (fileId == null) {
                return new ErrorResponse("id must be a number", ModelException.INVALID_FIELDS);
            }

            MusicFile file = this.getMusicFileFromRequest(req);
            return musicFileService.save(file);
        };
    }

    private Route create() {
        return (req, res) -> {
            MusicFile file = this.getMusicFileFromRequest(req);
            if (file.getId() != null) {
                return new ErrorResponse(
                  "When ID is already known, you must call PUT file/:id",
                  ModelException.INVALID_FIELDS
                );
            }
            return musicFileService.save(file);
        };
    }

    private Route deleteResource() {
        return (req, res) -> {
            Long fileId = this.getLong("id", req);
            if (fileId == null) {
                return new ErrorResponse("'id' must be a number", ModelException.INVALID_FIELDS);
            }

            return musicFileService.delete(fileId);
        };
    }

    public void routes() {
        path("/files", () -> {
            get(
                "/file/:id",
                "application/json",
                this.getById(),
                new JsonTransformer()
            );

            post(
              "/file",
              "application/json",
              this.create(),
              new JsonTransformer()
            );

            put(
              "/file/:id",
              "application/json",
              this.update(),
              new JsonTransformer()
            );

            delete(
              "/file/:id",
              "application/json",
              this.deleteResource(),
              new JsonTransformer()
            );
        });
    }
}