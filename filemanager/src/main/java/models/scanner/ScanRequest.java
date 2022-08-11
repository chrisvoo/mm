package models.scanner;

import models.Model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ScanRequest extends Model<ScanRequest> {
  private String directory;

  public ScanRequest() {
    this.requiredFields = List.of("directory");
  }

  public boolean isValid() {
    if (!super.isValid()) {
      // error types are already configured in the parent class.
      return false;
    }

    return this.errorCode == null;
  }

  public Path getDirectory() {
    return directory != null ? Paths.get(directory) : null;
  }

  public ScanRequest setDirectory(String directory) {
    this.directory = directory;
    return this;
  }

  public ScanRequest setDirectory(Path directory) {
    this.directory = directory.toAbsolutePath().toString();
    return this;
  }
}