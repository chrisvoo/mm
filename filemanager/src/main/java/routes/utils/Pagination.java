package routes.utils;

import com.google.inject.Inject;
import exceptions.ModelException;
import spark.Request;
import utils.logging.LoggerInterface;

import java.util.Base64;

public class Pagination extends QueryStringUtils {
  private String cursor;
  private int count;
  private String sortDir;

  private String sortBy;

  @Inject
  private LoggerInterface logger;

  public String getCursor() {
    return this.getCursor(false);
  }

  public String getCursor(boolean withDecoding) {
    if (this.cursor == null) {
      return null;
    }

    if (withDecoding) {
      return new String(Base64.getDecoder().decode(this.cursor));
    }

    return this.cursor;
  }

  public Pagination setCursor(String cursor) {
    return this.setCursor(cursor, false);
  }

  public Pagination setCursor(String cursor, boolean withEncoding) {
    if (cursor == null || cursor.trim().isBlank()) {
      this.cursor = null;
    } else if (withEncoding) {
      this.cursor = Base64.getEncoder().encodeToString(cursor.getBytes());
    } else {
      this.cursor = cursor;
    }

    return this;
  }

  public int getCount() {
    return count;
  }

  public Pagination setCount(int count) {
    this.count = count;
    return this;
  }

  public String getSortDir() {
    return sortDir;
  }

  public Pagination setSortDir(String sortDir) {
    this.sortDir = sortDir;
    return this;
  }

  public String getSortBy() {
    return sortBy;
  }

  public Pagination setSortBy(String sortBy) {
    this.sortBy = sortBy;
    return this;
  }

  private boolean isSortValid() {
    return this.sortDir.equalsIgnoreCase("desc") || this.sortDir.equalsIgnoreCase("asc") ||
      this.count <= 0;
  }

  public static Pagination fromRequest(Request req) {
    Pagination page = new Pagination();
    page
      .setCursor(page.getQsString(req,"cursor"))
      .setCount(page.getQsInt(req, "count", 10))
      .setSortBy(page.getQsString(req, "sort_by"))
      .setSortDir(page.getQsString(req, "sort_dir", "desc"));

    if (!page.isSortValid()) {
      throw new ModelException("Invalid pagination params", ModelException.INVALID_FIELDS);
    }

    return page;
  }
}