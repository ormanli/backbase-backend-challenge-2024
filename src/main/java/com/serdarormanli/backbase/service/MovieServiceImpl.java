package com.serdarormanli.backbase.service;

import com.serdarormanli.backbase.dto.CheckBestPictureOscarResponse;
import com.serdarormanli.backbase.dto.MovieDTO;
import com.serdarormanli.backbase.dto.RateMovieRequest;
import com.serdarormanli.backbase.model.MovieRating;
import com.serdarormanli.backbase.omdb.MovieResponse;
import com.serdarormanli.backbase.omdb.OMDBClient;
import com.serdarormanli.backbase.repository.BestPictureOscarRepository;
import com.serdarormanli.backbase.repository.MovieRatingAverageRepository;
import com.serdarormanli.backbase.repository.MovieRatingRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.Clock;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

@RequiredArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {

    private final BestPictureOscarRepository bestPictureOscarRepository;
    private final MovieRatingRepository movieRatingRepository;
    private final MovieRatingAverageRepository movieRatingAverageRepository;
    private final OMDBClient omdbClient;
    private final Clock clock;
    private final ExecutorService virtualThreadExecutorService;

    private final NumberFormat usdFormatter = NumberFormat.getCurrencyInstance(Locale.US);

    @Transactional(
            rollbackFor = Exception.class,
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            timeout = 30
    )
    @Override
    public void rateMovie(@NonNull @NotBlank String imdbId, @NonNull Long userId, @NonNull @Valid RateMovieRequest rateMovieRequest) {
        this.omdbClient.getMovieByImdbId(imdbId);

        var movieRating = new MovieRating();
        movieRating.setImdbId(imdbId);
        movieRating.setRating(rateMovieRequest.getRating());
        movieRating.setCreatedAt(this.clock.instant());
        movieRating.setApiUserId(userId);

        this.movieRatingRepository.save(movieRating);

        this.movieRatingAverageRepository.addRating(imdbId, rateMovieRequest.getRating());
    }

    @Override
    public CheckBestPictureOscarResponse checkBestPictureOscar(@NonNull @NotBlank String movieTitle) {
        return this.bestPictureOscarRepository.getYearOfWinner(movieTitle)
                .map(year -> new CheckBestPictureOscarResponse(Optional.of(year), true))
                .orElseGet(() -> new CheckBestPictureOscarResponse(null, false));
    }

    @SneakyThrows
    private Number parseBoxOffice(String boxOffice) {
        return usdFormatter.parse(boxOffice);
    }

    @SneakyThrows
    @Override
    public List<MovieDTO> top10ByBoxOffice() {
        var top10RatedMovies = this.movieRatingAverageRepository.findTopKRatedMovies(10);

        return this.virtualThreadExecutorService.submit(
                () -> top10RatedMovies.parallelStream()
                        .map(averageRating -> {
                            var movie = this.omdbClient.getMovieByImdbId(averageRating.getImdbId());
                            return new ResponseWrapper(averageRating.getRatingAverage(), movie);
                        })
                        .sorted(Comparator.<ResponseWrapper>comparingInt(responseWrapper -> this.parseBoxOffice(responseWrapper.response().boxOffice()).intValue()).reversed())
                        .map(responseWrapper -> {
                            var response = responseWrapper.response();
                            return new MovieDTO(response.title(), response.imdbID(), responseWrapper.averageRating(), response.boxOffice());
                        })
                        .toList()
        ).get();
    }

    private record ResponseWrapper(BigDecimal averageRating, MovieResponse response) {
    }
}
