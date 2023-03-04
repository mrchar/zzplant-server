package net.mrchar.zzplant.exception;

public class ForbiddenOperationException extends AbstractException {
    public static final String CODE = "ForbiddenOperationException";

    public ForbiddenOperationException() {
        super(CODE);
    }

    public ForbiddenOperationException(String message) {
        super(CODE, message);
    }

    public ForbiddenOperationException(String message, Throwable cause) {
        super(CODE, message, cause);
    }
}
