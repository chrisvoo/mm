package models.utils;

/**
 * Represent an error object, with a message, a constant boolean error field and an
 * optional code to identify a particular error.
 */
public class ErrorResponse {
    public static final int MISSING_RESOURCE = 1000;
    public static final int INVALID_RESOURCE = 1001;

    private String message;
    private final boolean error = true;
    private Integer code = null;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
