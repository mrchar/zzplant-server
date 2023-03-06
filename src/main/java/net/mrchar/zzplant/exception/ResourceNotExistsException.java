package net.mrchar.zzplant.exception;

public class ResourceNotExistsException extends AbstractException {
    public static final String CODE = "ResourceNotExistsException";

    public ResourceNotExistsException() {
        super(CODE);
    }

    public ResourceNotExistsException(String message) {
        super(CODE, message);
    }

    public ResourceNotExistsException(String message, Throwable cause) {
        super(CODE, message, cause);
    }
}
