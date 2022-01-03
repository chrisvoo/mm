package scanner;

import com.google.inject.Guice;
import com.google.inject.Injector;
import models.files.MusicFileSchema;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import src.DbHelper;
import utils.FileManagerModule;
import watcher.Watcher;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class WatcherTest {
  private static Watcher watcher;

  @BeforeAll
  static void initAll() throws URISyntaxException, IOException {
    DbHelper.emptyTable(new MusicFileSchema().tableName());

    Injector injector = Guice.createInjector(new FileManagerModule());
    watcher = injector.getInstance(Watcher.class);

    Path targetDir = Paths.get(Objects.requireNonNull(
      WatcherTest.class.getResource("/tree")
    ).toURI().getPath());

    watcher.registerAll(targetDir).start();
  }

  @AfterAll
  static void tearDown() {
    DbHelper.emptyTable(new MusicFileSchema().tableName());
    watcher.close();
  }

  @Test
  public void watcherTest() throws InterruptedException {
    Thread.sleep(100);

  }
}