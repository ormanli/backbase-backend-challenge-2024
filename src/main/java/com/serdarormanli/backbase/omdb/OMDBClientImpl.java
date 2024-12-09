package com.serdarormanli.backbase.omdb;

import com.serdarormanli.backbase.config.OMDBConfig;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@Component
public class OMDBClientImpl implements OMDBClient {
    private final RestClient restClient;
    private final OMDBConfig omdbConfig;

    private URI createURI(UriBuilder uriBuilder, String k, String v) {
        return uriBuilder
                .path("/")
                .queryParam(k, v)
                .queryParam("type", "movie")
                .queryParam("apikey", this.omdbConfig.getToken())
                .build();
    }

    @Cacheable(cacheNames = "omdb")
    @Override
    public MovieResponse getMovieByImdbId(@NonNull @NotBlank String imdbId) {
        var movieResponse = this.restClient.get()
                .uri(uriBuilder -> this.createURI(uriBuilder, "i", imdbId))
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), (request, response) -> {
                    throw new OMDBApiStatusCodeException(response.getStatusCode());
                })
                .body(MovieResponse.class);

        if (!StringUtils.isBlank(movieResponse.error())) {
            throw new OMDBApiErrorException(movieResponse.error());
        }

        return movieResponse;
    }
}
