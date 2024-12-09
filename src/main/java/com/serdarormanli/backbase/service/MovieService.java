package com.serdarormanli.backbase.service;

import com.serdarormanli.backbase.dto.CheckBestPictureOscarResponse;
import com.serdarormanli.backbase.dto.MovieDTO;
import com.serdarormanli.backbase.dto.RateMovieRequest;

import java.util.List;

public interface MovieService {
    void rateMovie(String imdbId, Long userId, RateMovieRequest rateMovieRequest);

    CheckBestPictureOscarResponse checkBestPictureOscar(String movieTitle);

    List<MovieDTO> top10ByBoxOffice();
}
