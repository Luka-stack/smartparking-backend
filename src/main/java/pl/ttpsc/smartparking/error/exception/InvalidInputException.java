package pl.ttpsc.smartparking.error.exception;

public class InvalidInputException extends ServiceException {

    public InvalidInputException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
