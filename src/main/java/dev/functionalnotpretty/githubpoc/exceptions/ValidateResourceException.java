package dev.functionalnotpretty.githubpoc.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidateResourceException extends Exception {
    private final List<String> errors;
    public ValidateResourceException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }
}
