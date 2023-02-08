package watcher;

import utils.di.GuiceUtils;
import utils.logging.LoggerInterface;
import watcher.handlers.EntryCreateHandler;
import watcher.handlers.EntryDeleteHandler;
import watcher.handlers.EntryHandler;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

import static java.nio.file.StandardWatchEventKinds.*;

public class WatcherHandler {

  private static LoggerInterface logger = GuiceUtils.getInstance(LoggerInterface.class);
  /**
   * Handle the key event
   * @param kind Type of event
   * @param child Resource involved in the event
   */
  public static void forEvent(
      WatchEvent.Kind<?> kind,
      Path child
  ) {
    if (kind == OVERFLOW) {
      return;
    }

    EntryHandler handler = null;

    try {
      if (kind == ENTRY_CREATE) {
        handler = new EntryCreateHandler(child);
      } else if (kind == ENTRY_DELETE) {
        handler = new EntryDeleteHandler(child);
      }

      if (handler != null) {
        handler.handle();
      }
    } catch (Exception x) {
      logger.severe(
        String.format(
          "Error for %s event %s, file %s: %s",
          x.getClass().getName(),
          kind.name(),
          child,
          x.getMessage())
      );
      x.printStackTrace();
    }
  }

}