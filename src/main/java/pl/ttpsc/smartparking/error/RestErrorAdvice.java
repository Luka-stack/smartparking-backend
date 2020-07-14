package pl.ttpsc.smartparking.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.ttpsc.smartparking.error.exception.*;

@ControllerAdvice
public class RestErrorAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NotFoundAccessException.class, NotFoundPlateException.class,
                               NotFoundErrorCodeException.class})
    public ResponseEntity<Error> handleNotFound(ServiceException exception) {

        Error error = new Error(exception.getMessage(), exception.getErrorCode());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {InvalidInputException.class})
    public ResponseEntity<Error> handleInvalidInput(ServiceException exception) {

        Error error = new Error(exception.getMessage(), exception.getErrorCode());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
