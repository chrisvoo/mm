package utils;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {
  /**
   * It tests that the specified file is an MP3 file (it must exist).
   * @param path The resource.
   * @return true if it's an MP3 file, false otherwise
   */
  public static boolean isMp3(Path path) {
    return Files.isRegularFile(path) && path.getFileName().toString().toLowerCase().endsWith(".mp3");
  }

  /**
   * Just tests that a path ends with .mp3 extension.
   * @param path The resource.
   * @return true if the name ends with the desired extension.
   */
  public static boolean isMp3FileName(Path path) {
    return path.getFileName().toString().toLowerCase().endsWith(".mp3");
  }
}