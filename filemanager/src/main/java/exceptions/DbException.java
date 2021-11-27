package exceptions;

/**
 * Custom exception for env errors
 */
public class DbException extends FileManagerException {
    public static final int MISSING_DRIVER = 200;
    public static final int CONNECTION_ERROR = 201;
    public static final int CLOSING_CONNECTION_ERROR = 202;

    public DbException(String message, int code) {
        super(message, code);
    }
}
