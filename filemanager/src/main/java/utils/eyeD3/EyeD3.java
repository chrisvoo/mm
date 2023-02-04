package utils.eyeD3;

import com.google.gson.Gson;
import com.google.inject.Inject;
import models.files.MusicFile;
import utils.logging.LoggerInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class EyeD3 {

  @Inject private static LoggerInterface logger;

  public static MusicFile parse(Path path) {
    try {
      ProcessBuilder builder = new ProcessBuilder().command("eyeD3", "--plugin=json", path.toString());
      Process process = builder.start();
      String s = new BufferedReader(new InputStreamReader(process.getInputStream()))
        .lines()
        .collect(Collectors.joining());

      if (!s.isBlank()) {
        EyeD3Result result = new Gson().fromJson(s, EyeD3Result.class);
        if (result.hasInvalidValues()) {
          logger.warning(
            String.format(
              "Invalid values for %s: duration -> %f, size -> %d",
              path,
              result.info.time_secs,
              result.info.size_bytes
            )
          );
        }

        return result.toMusicFile();
      }

      String error = new BufferedReader(new InputStreamReader(process.getErrorStream()))
        .lines()
        .collect(Collectors.joining());
      logger.warning(String.format("EyeD3 error for %s: %s", path, error));

      return null;
    } catch (IOException io) {
      logger.severe("Cannot parse " + path + ": " + io.getMessage());
      return null;
    }
  }
}