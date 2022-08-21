package src;

import models.MusicFileSchemaTest;
import routes.BandRoutesTest;
import routes.MusicFilesRoutesTest;
import routes.ScannerRoutesTest;
import scanner.WatcherTest;
import utils.FileManagerModule;

/**
 * Inherits everything defined in FileManagerModule, but also defines the injections rules for tests.
 */
public class TestModule extends FileManagerModule {
  protected void configure() {
    super.configure();
    requestStaticInjection(
      BandRoutesTest.class, MusicFileSchemaTest.class, MusicFilesRoutesTest.class,
      WatcherTest.class, ScannerRoutesTest.class
    );
  }
}