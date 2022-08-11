package routes;

import com.google.inject.Inject;
import exceptions.ModelException;
import models.files.MusicFile;
import models.utils.ErrorResponse;
import routes.utils.Pagination;
import services.MusicFileService;
import spark.Request;
import spark.Route;
import utils.JsonTransformer;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import static spark.Spark.*;

public class MusicFilesRoutes extends routes.Route implements Router {
    private final MusicFileService musicFileService;

    @Inject
    public MusicFilesRoutes(MusicFileService musicFileService) {
        this.musicFileService = musicFileService;
    }

    private MusicFile getMusicFileById(Request req) {
        Long fileId = this.getLongParam("id", req);
        if (fileId == null) {
            throw new ModelException("id must be a number", ModelException.INVALID_FIELDS);
        }

        MusicFile file = musicFileService.getById(fileId);
        if (file == null) {
            throw new ModelException(
              String.format("No file with id %d was found", fileId),
              ModelException.INVALID_FIELDS
            );
        }

        return file;
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
            try {
                return this.getMusicFileById(req);
            } catch (ModelException e) {
                return new ErrorResponse(e.getMessage(), e.getCode());
            }
        };
    }

    private Route streamById() {
        return (req, res) -> {
            RandomAccessFile raf = null;
            OutputStream stream = null;
            try {
                MusicFile mp3 = this.getMusicFileById(req);
                File file = mp3.toFile();
                raf = new RandomAccessFile(file, "r");

                int off = 0;
                int to = (int) file.length();
                String rangeHeader = req.headers("Range");

                HttpServletResponse response = res.raw();

                if (rangeHeader != null) {
                    String boundaries = rangeHeader.split("=")[1];

                    String from = String.valueOf(boundaries.charAt(0));
                    off = Integer.parseInt(from);

                    if (!boundaries.equals("0-")) {
                        String t = rangeHeader.split("=")[1].split("-")[1];
                        to = Integer.parseInt(t);

                        response.setHeader("Content-Range:", "bytes " + off + "-" + to + "/" + (to - off));
                        response.setStatus(206);
                    }
                }

                byte[] data = new byte[to - off];
                raf.readFully(data, off, to - off);

                response.setContentType("audio/mpeg");
                response.setHeader("Accept-Ranges","bytes");
                response.setContentLength(data.length);
                response.setHeader("Cache-Control", "no-cache, private");
                response.setHeader("Expires", "0");
                response.setHeader("Content-Transfer-Encoding", "binary");
                response.setHeader("Transfer-Encoding", "chunked");

                stream = response.getOutputStream();
                stream.write(data);

                return null;
            } catch (ModelException e) {
                return new ErrorResponse(e.getMessage(), e.getCode());
            } finally {
                if (raf != null) {
                    raf.close();
                }

                if (stream != null) {
                    stream.close();
                }
            }
        };
    }

    private Route getAll() {
        return (req, res) -> {
            Pagination pagination = Pagination.fromRequest(req);
            return musicFileService.getAll(pagination);
        };
    }

    private Route update() {
        return (req, res) -> {
            Long fileId = this.getLongParam("id", req);
            if (fileId == null) {
                return new ErrorResponse("id must be a number", ModelException.INVALID_FIELDS);
            }

            MusicFile file = this.getMusicFileFromRequest(req);
            file.setId(fileId);
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
            Long fileId = this.getLongParam("id", req);
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

            get(
              "/stream/:id",
              this.streamById()
            );

            get(
              "/list",
              "application/json",
              this.getAll(),
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