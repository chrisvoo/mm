package exceptions;

public class FileManagerException extends RuntimeException {
    private int code = -1;

    public FileManagerException(String message) {
        super(message);
    }

    public FileManagerException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
