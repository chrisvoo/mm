package watcher.handlers;

import services.MusicFileService;
import services.StatsService;
import utils.di.GuiceUtils;
import utils.logging.LoggerInterface;
import watcher.Watcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

public abstract class EntryHandler {
  protected Path child;

  protected final MusicFileService musicFileService = GuiceUtils.getInstance(MusicFileService.class);
  protected final StatsService statsService = GuiceUtils.getInstance(StatsService.class);
  protected final Watcher watcher = GuiceUtils.getInstance(Watcher.class);
  protected final LoggerInterface logger = GuiceUtils.getInstance(LoggerInterface.class);

  protected EntryHandler(Path child) {
    this.child = child;
  }

  public abstract void handleDirectory() throws IOException;
  public abstract void handleFile();
  public void handle() throws IOException {
    if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
      handleDirectory();
    } else {
      handleFile();
    }
  };

}