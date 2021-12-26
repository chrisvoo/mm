package routes.utils;

import exceptions.ModelException;
import spark.Request;

public class QueryStringUtils {
  /**
   * Retrieve an integer query string parameter's value.
   * @param req The request object.
   * @param paramName The parameter's name.
   * @param defaultValue The optional default value (or NULL if missing).
   * @return The query string parameter's value.
   */
  protected Integer getQsInt(Request req, String paramName, Integer defaultValue) {
    String theParam = req.queryParamOrDefault(paramName, defaultValue != null ? defaultValue.toString() : null);
    if (theParam != null) {
      try {
        return Integer.valueOf(theParam);
      } catch (NumberFormatException n) {
        throw new ModelException(paramName, ModelException.INVALID_FIELDS);
      }
    }

    return null;
  }

  /**
   * Retrieve an integer query string parameter's value.
   * @param req The request object.
   * @param paramName The parameter's name.
   * @return The query string parameter's value.
   */
  protected Integer getQsInt(Request req, String paramName) {
    return this.getQsInt(req, paramName, null);
  }

  /**
   * Retrieve a boolean query string parameter's value.
   * @param req The request object.
   * @param paramName The parameter's name.
   * @param defaultValue The optional default value (or NULL if missing).
   * @return The query string parameter's value.
   */
  protected Boolean getQsBool(Request req, String paramName, Boolean defaultValue) {
    String theParam = req.queryParamOrDefault(paramName, defaultValue != null ? defaultValue.toString() : null);
    if (theParam != null) {
      if (
        theParam.toLowerCase().trim().equals("true") ||
          theParam.toLowerCase().trim().equals("false")
      ) {
        return Boolean.valueOf(theParam);
      } else {
        throw new ModelException(paramName, ModelException.INVALID_FIELDS);
      }
    }

    return null;
  }

  /**
   * Retrieve a boolean query string parameter's value.
   * @param req The request object.
   * @param paramName The parameter's name.
   * @return The query string parameter's value.
   */
  protected Boolean getQsBool(Request req, String paramName) {
    return this.getQsBool(req, paramName, null);
  }

  /**
   * Retrieve a textual query string parameter's value.
   * @param req The request object.
   * @param paramName The parameter's name.
   * @param defaultValue The optional default value (or NULL if missing).
   * @return The query string parameter's value.
   */
  protected String getQsString(Request req, String paramName, String defaultValue) {
    String theParam = req.queryParamOrDefault(paramName, defaultValue);
    if (theParam != null) {
      return theParam.trim();
    }

    return null;
  }

  /**
   * Retrieve a textual query string parameter's value.
   * @param req The request object.
   * @param paramName The parameter's name.
   * @return The query string parameter's value.
   */
  protected String getQsString(Request req, String paramName) {
    return req.queryParamOrDefault(paramName, null);
  }
}