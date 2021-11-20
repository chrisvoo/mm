package exceptions;

/**
 * Custom exception for env errors
 */
public class EnvException extends RuntimeException {
    public EnvException(String field) {
        super( String.format("Missing required env variable: %s", field));
    }
}
