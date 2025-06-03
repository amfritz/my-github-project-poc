package dev.functionalnotpretty.githubpoc.exceptions;

import java.time.Instant;
import java.util.List;

public record ControllerExceptionError(
        String path,
        String message,
        int statusCode,
        Instant timestamp,
        List<String> errors
) {

}
