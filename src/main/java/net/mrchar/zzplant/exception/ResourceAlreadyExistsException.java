package net.mrchar.zzplant.exception;

/**
 * 该错误表示要创建的资源已经存在，进而无法继续创建相同的资源。
 */
public class ResourceAlreadyExistsException extends AbstractException {
    public static final String CODE = "ResourceAlreadyExistsException";

    public ResourceAlreadyExistsException() {
        super(CODE);
    }

    public ResourceAlreadyExistsException(String message) {
        super(CODE, message);
    }

    public ResourceAlreadyExistsException(String message, Throwable cause) {
        super(CODE, message, cause);
    }
}
