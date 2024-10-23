package hhplus.booking.config.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
class ApiControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity.status(500).body(new ErrorResponse("500", e.getMessage()));
    }

//    @ExceptionHandler(value = AuthenticationException.class)
//    public ResponseEntity<ErrorResponse> handleAuthorizationException(Exception e) {
//        return ResponseEntity.status(200).body(new ErrorResponse("401", e.getMessage()));
//    }

    @ExceptionHandler(value = MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(Exception e) {
        return ResponseEntity.status(200).body(new ErrorResponse("401", e.getMessage()));
    }
}