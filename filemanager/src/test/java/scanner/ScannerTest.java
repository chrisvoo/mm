package scanner;

import com.google.inject.Guice;
import com.google.inject.Injector;
import models.band.BandSchema;
import models.scanner.ScanOp;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import src.DbHelper;
import utils.FileManagerModule;

import java.net.URISyntaxException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class ScannerTest {

  @BeforeAll
  static void initAll() {
    DbHelper.emptyTable(new BandSchema().tableName());
  }

  @AfterAll
  static void tearDown() {
    DbHelper.emptyTable(new BandSchema().tableName());
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
    assertEquals(0, result.getTotalElapsedTime());
    assertEquals(42656676, result.getTotalBytes());
    assertNull(result.getScanErrors());
    rep.publishEntry(result.toString());
  }
}