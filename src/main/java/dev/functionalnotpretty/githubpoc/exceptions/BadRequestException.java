package dev.functionalnotpretty.githubpoc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    //private static final long serialVersionUID = 1L;
    public BadRequestException(String message) {
        super(message);
    }
}