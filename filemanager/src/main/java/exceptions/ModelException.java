package exceptions;

import java.util.Map;
import java.util.stream.Collectors;

public class ModelException extends FileManagerException {
    public static final int MISSING_REQUIRED_FIELDS = 300;
    public static final int INVALID_FIELDS = 301;

    /**
     * Converts a map to a string
     * @param map Errors map
     * @return A string representation of the errors map.
     */
    private static String mapToString(Map<String, String> map) {
        return map.keySet().stream()
                .map(key -> key + "=" + map.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }

    public ModelException(String error, int errorCode) {
        super(error, errorCode);
    }

    public ModelException(Map<String, String> errors, int errorCode) {
        super(ModelException.mapToString(errors), errorCode);
    }
}