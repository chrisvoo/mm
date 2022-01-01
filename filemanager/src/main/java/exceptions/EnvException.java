package exceptions;

/**
 * Custom exception for env errors
 */
public class EnvException extends FileManagerException {
    public static final int ENV_ERROR = 100;
    public static final int MUSIC_DIR_INVALID = 101;

    public EnvException(String field) {
        super( String.format("Missing required env variable: %s", field));
    }

    public EnvException(String message, int code) {
        super(message, code);
    }

    public int getCode() {
        return EnvException.ENV_ERROR;
    }
}