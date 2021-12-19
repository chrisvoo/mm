package scanner;

import models.scanner.ScanOp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import java.net.URISyntaxException;
import java.util.Objects;

public class ScannerTest {

  @Test
  public void testScanDirectory(TestReporter rep) throws URISyntaxException, InterruptedException {
    String targetDir = Objects.requireNonNull(
      getClass().getResource("/tree")
    ).toURI().getPath();

    Scanner scanner = new Scanner().setTargetDir(targetDir);
    ScanOp result = scanner.scan();
    rep.publishEntry(result.toString());
  }
}