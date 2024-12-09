package com.serdarormanli.backbase.omdb;

import lombok.NonNull;
import org.springframework.http.HttpStatusCode;

public class OMDBApiStatusCodeException extends RuntimeException {
    public OMDBApiStatusCodeException(@NonNull HttpStatusCode statusCode) {
        super("OMDB API returned status code: %d, ".formatted(statusCode.value()));
    }
}
