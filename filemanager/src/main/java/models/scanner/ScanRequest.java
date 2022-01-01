package models.scanner;

import models.Model;

import java.nio.file.Path;
import java.util.List;

public class ScanRequest extends Model<ScanRequest> {
  private Path directory;

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
    return directory;
  }

  public ScanRequest setDirectory(Path directory) {
    this.directory = directory;
    return this;
  }
}