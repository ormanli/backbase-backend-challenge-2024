package com.serdarormanli.backbase.omdb;

import lombok.NonNull;

public class OMDBApiErrorException extends RuntimeException {
    public OMDBApiErrorException(@NonNull String message) {
        super("OMDB API returned error: %s".formatted(message));
    }
}
