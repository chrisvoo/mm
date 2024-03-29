package scanner;

import com.google.inject.Guice;
import com.google.inject.Injector;
import models.files.MusicFileSchema;
import models.scanner.ScanOp;
import models.scanner.ScanOpSchema;
import models.stats.Stats;
import models.stats.StatsSchema;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import services.StatsService;
import src.DbHelper;
import utils.di.FileManagerModule;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ScannerTest {

  @BeforeAll
  static void initAll() {
    DbHelper.emptyTables(List.of(
      MusicFileSchema.TABLE_NAME,
      ScanOpSchema.TABLE_NAME,
      StatsSchema.TABLE_NAME
    ));
  }

  @AfterAll
  static void tearDown() {

  }

  @Test
  public void testScanDirectory(TestReporter rep) throws URISyntaxException, InterruptedException {
    String targetDir = Objects.requireNonNull(
      getClass().getResource("/tree")
    ).toURI().getPath();

    Injector injector = Guice.createInjector(new FileManagerModule());
    Scanner scanner = injector.getInstance(Scanner.class);

    scanner.setTargetDir(targetDir);
    ScanOp result = scanner.scan();

    assertNotNull(result.getId());
    assertEquals(14, result.getTotalFilesInserted());
    assertEquals(14, result.getTotalFilesScanned());
    assertEquals(1, result.getTotalElapsedTime());
    assertEquals(42656676, result.getTotalBytes());
    rep.publishEntry(result.toString());

    StatsService statsService = injector.getInstance(StatsService.class);
    Stats stats = statsService.getStats();
    assertEquals(14, stats.getTotalFiles());
    assertEquals(42656676, stats.getTotalBytes());
    assertNotNull(stats.getLastUpdate());
  }
}