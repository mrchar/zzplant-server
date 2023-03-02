package net.mrchar.zzplant.exception;

public class UnExpectedException extends AbstractException {
    public static final String CODE = "UnexpectedException";

    public UnExpectedException() {
        super(CODE);
    }

    public UnExpectedException(String message) {
        super(CODE, message);
    }

    public UnExpectedException(String message, Throwable cause) {
        super(CODE, message, cause);
    }
}
