package net.mrchar.zzplant.exception;

public class BalanceIsNotEnoughException extends AbstractException {
    public static final String CODE = "BalanceIsNotEnough";

    public BalanceIsNotEnoughException() {
        super(CODE);
    }

    public BalanceIsNotEnoughException(String message) {
        super(CODE, message);
    }

    public BalanceIsNotEnoughException(String message, Throwable cause) {
        super(CODE, message, cause);
    }
}
