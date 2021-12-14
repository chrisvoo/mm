package models.scanner;

import com.google.gson.Gson;
import models.Model;

import java.util.List;

public class ScanRequest extends Model {
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

  public String getDirectory() {
    return directory;
  }

  public ScanRequest setDirectory(String directory) {
    this.directory = directory;
    return this;
  }

  public static ScanRequest fromJson(String json) {
    return new Gson().fromJson(json, ScanRequest.class);
  }
}