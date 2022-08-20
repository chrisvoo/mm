package utils.logging;

public interface LoggerInterface {
  void fine(String message);
  void info(String message);
  void warning(String message);
  void severe(String message);
}