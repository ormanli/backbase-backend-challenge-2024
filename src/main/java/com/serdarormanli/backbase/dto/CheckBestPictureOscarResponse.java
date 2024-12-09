package com.serdarormanli.backbase.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Optional;

public record CheckBestPictureOscarResponse(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Optional<String> year,
        boolean isWinner
) {
}
