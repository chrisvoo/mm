package routes;

import com.google.inject.Inject;
import services.MusicFileService;
import spark.Route;
import utils.JsonTransformer;

import static spark.Spark.get;
import static spark.Spark.path;

public class MusicFilesRoutes implements Router {
    private final MusicFileService musicFileService;

    @Inject
    public MusicFilesRoutes(MusicFileService musicFileService) {
        this.musicFileService = musicFileService;
    }

    private Route getById() {
        return (req, res) -> musicFileService.getById(5);
    }

    public void routes() {
        path("/files", () -> {
            get(
                "/file/:id",
                "application/json",
                this.getById(),
                new JsonTransformer()
            );
        });
    }
}
