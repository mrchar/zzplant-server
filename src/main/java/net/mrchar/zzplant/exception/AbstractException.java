package net.mrchar.zzplant.exception;

public class AbstractException extends RuntimeException {
    private final String code;

    public String getCode() {
        return code;
    }

    protected AbstractException(String code) {
        this.code = code;
    }

    protected AbstractException(String code, String message) {
        super(message);
        this.code = code;
    }

    protected AbstractException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
