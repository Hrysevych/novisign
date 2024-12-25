package task.novisign;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    // Handle HttpMessageNotReadableException with a specific response
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleMalformedRequestBody(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(
                "The request body is malformed. Please check the structure of the provided data.",
                HttpStatus.BAD_REQUEST
        );
    }

    // General handler for other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(
                "An unexpected error occurred. Please contact system administrator.",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
