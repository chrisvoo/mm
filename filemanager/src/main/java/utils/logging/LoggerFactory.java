package utils.logging;

import com.google.inject.Inject;
import utils.EnvVars;

import java.util.Date;
import java.util.logging.*;

public class LoggerFactory implements LoggerInterface {
  private EnvVars envVars;
  private Logger logger;

  /**
   * Guice has java.util.logging.logger as built-in injection class, so I defined a wrapper around Logger with a config
   * defined programmatically.
   * @param envVars
   */
  @Inject
  public LoggerFactory(EnvVars envVars) {
    this.envVars = envVars;

    Level logLevel = isTestOrDevEnv() ? Level.FINE : Level.INFO;
    this.logger = Logger.getAnonymousLogger();
    logger.setLevel(logLevel);

    ConsoleHandler handler = new ConsoleHandler();
    handler.setLevel(logLevel);
    handler.setFormatter(new SimpleFormatter() {
      private static final String format = "[%1$tF %1$tT - %3$-1s - %2$s] %4$s %n";

      @Override
      public synchronized String format(LogRecord lr) {
        return String.format(
          format,
          new Date(lr.getMillis()),
          Thread.currentThread().getStackTrace()[9].getClassName(), // the class effectively using the logger
          lr.getLevel().getLocalizedName(),
          lr.getMessage()
        );
      }
    });
    logger.setUseParentHandlers(false); // avoid duplicated messages for INFO and above levels.
    logger.getHandlers();
    logger.addHandler(handler);
  }

  private boolean isTestOrDevEnv() {
    return this.envVars.getEnvironment() == EnvVars.Environments.TEST ||
      this.envVars.getEnvironment() == EnvVars.Environments.DEVELOPMENT;
  }

  public void info(String msg) {
    this.logger.info(msg);
  }

  public void fine(String msg) {
    this.logger.fine(msg);
  }

  public void severe(String msg) {
    this.logger.severe(msg);
  }

  public void warning(String msg) {
    this.logger.warning(msg);
  }
}