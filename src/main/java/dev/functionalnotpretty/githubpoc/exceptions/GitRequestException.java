package dev.functionalnotpretty.githubpoc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE)
public class GitRequestException extends RuntimeException {
    //private static final long serialVersionUID = 1L;
    public GitRequestException(String message) {
        super(message);
    }
}