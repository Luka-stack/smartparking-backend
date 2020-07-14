package pl.ttpsc.smartparking.error.exception;

public class NotFoundAccessException extends ServiceException {

    public NotFoundAccessException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
