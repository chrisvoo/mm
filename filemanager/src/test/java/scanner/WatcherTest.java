package scanner;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import models.files.MusicFile;
import models.files.MusicFileSchema;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import routes.utils.PaginatedResponse;
import routes.utils.Pagination;
import services.MusicFileService;
import src.DbHelper;
import utils.di.FileManagerModule;
import watcher.Watcher;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WatcherTest {
  private static Watcher watcher;
  private static Path targetDir;
  private static File destDir;
  private static MusicFileService musicFileService;

  @Inject
  private static MusicFileSchema schema;


  @BeforeAll
  static void initAll() throws URISyntaxException, IOException {
    DbHelper.emptyTable(schema.tableName());

    Injector injector = Guice.createInjector(new FileManagerModule());
    watcher = injector.getInstance(Watcher.class);
    musicFileService = injector.getInstance(MusicFileService.class);

    targetDir = Paths.get(Objects.requireNonNull(
      WatcherTest.class.getResource("/tree")
    ).toURI().getPath());

    destDir = new File(targetDir.toFile() + "/copy");
    watcher.registerAll(targetDir).start();
  }

  @AfterAll
  static void tearDown() {
    watcher.close();
    utils.FileUtils.deleteResource(Paths.get(targetDir.toFile() + "/copy"));
  }

  @Test
  public void watcherTest() throws InterruptedException, IOException {
    Thread.sleep(400);

    FileUtils.copyDirectory(targetDir.toFile(), destDir);

    Thread.sleep(400);

    PaginatedResponse<MusicFile> files = musicFileService.getAll(
      new Pagination().setCount(20).setSortDir("desc")
    );
    assertEquals(14, files.getItems().size());
  }
}