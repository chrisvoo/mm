package utils;

import com.google.inject.Inject;
import utils.logging.LoggerInterface;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileUtils {
  private static final long BYTE = 1L;
  private static final long KiB = BYTE << 10;
  private static final long MiB = KiB << 10;
  private static final long GiB = MiB << 10;
  private static final long TiB = GiB << 10;
  private static final long PiB = TiB << 10;
  private static final long EiB = PiB << 10;

  private static final DecimalFormat DEC_FORMAT = new DecimalFormat("#.##");

  private static String formatSize(long size, long divider, String unitName) {
    return DEC_FORMAT.format((double) size / divider) + " " + unitName;
  }

  @Inject
  private static LoggerInterface logger;

  public static String formatBytes(long size) {
    if (size < 0) {
      throw new IllegalArgumentException("Invalid file size: " + size);
    }
    if (size < 1024) return size + " Bytes";
    int unitIdx = (63 - Long.numberOfLeadingZeros(size)) / 10;
    return formatSize(size, 1L << (unitIdx * 10), " KMGTPE".charAt(unitIdx) + "iB");
  }

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

  /**
   * Collects all mp3 files recursively found inside a directory (scanning also the subdirs)
   * @param path The directory or the file.
   * @return A list of Path representing MP3 files
   */
  public static List<Path> listMP3Files(Path path, List<Path> files) throws IOException {
    if (files == null) {
      files = new ArrayList<>();
    }

    try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
      for (Path entry : stream) {
        if (Files.isDirectory(entry)) {
          listMP3Files(entry, files);
        }
        if (FileUtils.isMp3FileName(entry)) {
          files.add(entry);
        }
      }
    }
    return files;
  }

  public static List<Path> listMP3Files(Path path) throws IOException {
    return listMP3Files(path, null);
  }

  /**
   * Delete a file or a directory (even if it's not empty)
   * @param resource The directory or file to be deleted
   */
  public static void deleteResource(Path resource) {
    try {
      if (Files.exists(resource)) {
        if (Files.isDirectory(resource)) {
          Files.walk(resource)
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
        } else {
          Files.delete(resource);
        }
      }
    } catch (IOException e) {
      logger.severe("Cannot delete " + resource + ": " + e.getMessage());
    }
  }
}