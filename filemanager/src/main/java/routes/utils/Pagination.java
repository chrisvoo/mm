package routes.utils;

import exceptions.ModelException;
import spark.Request;

import java.util.Base64;
import java.util.logging.Logger;

public class Pagination extends QueryStringUtils {
  private String cursor;
  private int count;
  private String sortDir;

  private static final Logger logger = Logger.getLogger(Pagination.class.getName());

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

  private boolean isSortValid() {
    return this.sortDir.equalsIgnoreCase("desc") || this.sortDir.equalsIgnoreCase("asc");
  }

  public static Pagination fromRequest(Request req) {
    Pagination page = new Pagination();
    page
      .setCursor(page.getQsString(req,"cursor"))
      .setCount(page.getQsInt(req, "count", 10))
      .setSortDir(page.getQsString(req, "sort_dir", "desc"));

    if (!page.isSortValid()) {
      throw new ModelException("sort_dir", ModelException.INVALID_FIELDS);
    }

    return page;
  }
}