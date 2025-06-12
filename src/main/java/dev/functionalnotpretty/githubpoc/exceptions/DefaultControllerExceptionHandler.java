package dev.functionalnotpretty.githubpoc.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.List;

@ControllerAdvice
public class DefaultControllerExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ControllerExceptionError> handle(
            ResourceNotFoundException e,
            HttpServletRequest request) {
        ControllerExceptionError exceptionError = new ControllerExceptionError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                Instant.now(),
                List.of()
        );
        return new ResponseEntity<>(exceptionError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ControllerExceptionError> handle(
            BadRequestException e,
            HttpServletRequest request) {
        ControllerExceptionError exceptionError = new ControllerExceptionError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                Instant.now(),
                List.of()
        );
        return new ResponseEntity<>(exceptionError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidateResourceException.class)
    public ResponseEntity<ControllerExceptionError> handle(
            ValidateResourceException e,
            HttpServletRequest request) {
        ControllerExceptionError apiError = new ControllerExceptionError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                Instant.now(),
                e.getErrors()
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GitRequestException.class)
    public ResponseEntity<ControllerExceptionError> handle(
            GitRequestException e,
            HttpServletRequest request) {
        ControllerExceptionError exceptionError = new ControllerExceptionError(
                request.getRequestURI(),
                e.getMessage(),
                e.getCode(),
                Instant.now(),
                List.of()
        );
        return new ResponseEntity<>(exceptionError, HttpStatus.valueOf(e.getCode()));
    }
}
