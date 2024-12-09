package com.serdarormanli.backbase.controller;

import com.serdarormanli.backbase.dto.CheckBestPictureOscarResponse;
import com.serdarormanli.backbase.dto.MovieDTO;
import com.serdarormanli.backbase.dto.RateMovieRequest;
import com.serdarormanli.backbase.dto.UserDetailsWithID;
import com.serdarormanli.backbase.service.MovieService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping(value = "/movies", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping(value = "/{imdbId}/ratings", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(value = OK)
    public void rateMovie(@PathVariable String imdbId, @Valid @RequestBody RateMovieRequest rateMovieRequest, @AuthenticationPrincipal UserDetailsWithID userDetails) {
        this.movieService.rateMovie(imdbId, userDetails.getID(), rateMovieRequest);
    }

    @GetMapping(value = "/bestPictureOscar")
    @ResponseStatus(value = OK)
    public CheckBestPictureOscarResponse checkBestPictureOscar(@RequestParam @NotBlank String movieTitle) {
        return this.movieService.checkBestPictureOscar(movieTitle);
    }

    @GetMapping("/top10/boxOffice")
    @ResponseStatus(value = OK)
    public List<MovieDTO> top10ByBoxOffice() {
        return this.movieService.top10ByBoxOffice();
    }
}
