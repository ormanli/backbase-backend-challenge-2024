package com.serdarormanli.backbase.omdb;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public interface OMDBClient {
    MovieResponse getMovieByImdbId(@NonNull @NotBlank String imdbId);
}
