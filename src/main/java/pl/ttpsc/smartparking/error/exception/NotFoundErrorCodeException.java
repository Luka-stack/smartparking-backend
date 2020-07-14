package pl.ttpsc.smartparking.error.exception;

public class NotFoundErrorCodeException extends ServiceException {

    public NotFoundErrorCodeException(String message) {
        super(message, null);
    }
}
