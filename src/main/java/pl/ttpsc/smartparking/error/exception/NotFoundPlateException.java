package pl.ttpsc.smartparking.error.exception;

public class NotFoundPlateException extends ServiceException {

    public NotFoundPlateException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
