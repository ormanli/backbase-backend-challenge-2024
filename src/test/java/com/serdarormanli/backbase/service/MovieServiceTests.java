package com.serdarormanli.backbase.service;

import com.serdarormanli.backbase.dto.MovieDTO;
import com.serdarormanli.backbase.dto.RateMovieRequest;
import com.serdarormanli.backbase.model.MovieRating;
import com.serdarormanli.backbase.model.MovieRatingAverage;
import com.serdarormanli.backbase.omdb.MovieResponse;
import com.serdarormanli.backbase.omdb.OMDBClient;
import com.serdarormanli.backbase.repository.BestPictureOscarRepository;
import com.serdarormanli.backbase.repository.MovieRatingAverageRepository;
import com.serdarormanli.backbase.repository.MovieRatingRepository;
import lombok.SneakyThrows;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.NumberFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.Executors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, InstancioExtension.class})
public class MovieServiceTests {

    private static final Instant now = Instant.now();
    private static final NumberFormat usdFormatter = NumberFormat.getCurrencyInstance(Locale.US);

    @TestConfiguration
    static class MovieServiceTestsContextConfiguration {

        @Bean
        public MovieService movieService(
                BestPictureOscarRepository mockBestPictureRepository,
                MovieRatingRepository mockMovieRatingRepository,
                MovieRatingAverageRepository mockMovieRatingAverageRepository,
                OMDBClient mockOMDBClient) {

            return new MovieServiceImpl(
                    mockBestPictureRepository,
                    mockMovieRatingRepository,
                    mockMovieRatingAverageRepository,
                    mockOMDBClient,
                    Clock.fixed(now, ZoneOffset.UTC),
                    Executors.newVirtualThreadPerTaskExecutor());
        }
    }

    @Autowired
    private MovieService movieService;

    @MockBean
    BestPictureOscarRepository mockBestPictureRepository;

    @MockBean
    MovieRatingRepository mockMovieRatingRepository;

    @MockBean
    MovieRatingAverageRepository mockMovieRatingAverageRepository;

    @MockBean
    OMDBClient mockOMDBClient;

    @Test
    void noRatedMovies() {
        when(this.mockMovieRatingAverageRepository.findTopKRatedMovies(eq(Integer.valueOf(10))))
                .thenReturn(Collections.emptyList());

        var movies = this.movieService.top10ByBoxOffice();

        assertThat(movies).isEmpty();
    }

    @Test
    void ratedMovies() {
        var averageMovieRatings = Instancio.ofList(MovieRatingAverage.class)
                .size(10)
                .create();

        var movieIds = new LinkedList<String>();
        averageMovieRatings.forEach(movieRating -> {
            movieIds.add(movieRating.getImdbId());
        });

        var movieResponses = Instancio.ofList(MovieResponse.class)
                .size(10)
                .supply(field(MovieResponse::imdbID), movieIds::removeFirst)
                .supply(field(MovieResponse::boxOffice), random -> usdFormatter.format(random.intRange(100000, 1000000)))
                .create();

        movieResponses.sort(Comparator.<MovieResponse>comparingInt(value -> parseBoxOffice(value.boxOffice())).reversed());

        var movieResponsesById = movieResponses.stream()
                .collect(toMap(MovieResponse::imdbID, identity()));

        when(this.mockMovieRatingAverageRepository.findTopKRatedMovies(eq(Integer.valueOf(10))))
                .thenReturn(averageMovieRatings);

        when(this.mockOMDBClient.getMovieByImdbId(anyString()))
                .thenAnswer(i -> movieResponsesById.get(i.getArgument(0)));

        var movies = this.movieService.top10ByBoxOffice();

        assertThat(movies).hasSize(10);
        assertThat(movies).map(MovieDTO::imdbId).containsExactlyElementsOf(movieResponses.stream().map(MovieResponse::imdbID).toList());
    }

    @Test
    void rateMovie() {
        var rateMovieRequest = new RateMovieRequest();
        rateMovieRequest.setRating(9);

        this.movieService.rateMovie("t1", 1L, rateMovieRequest);

        var movieRating = new MovieRating();
        movieRating.setRating(9);
        movieRating.setCreatedAt(now);
        movieRating.setApiUserId(1L);
        movieRating.setImdbId("t1");

        verify(this.mockOMDBClient, times(1)).getMovieByImdbId(eq("t1"));
        verify(this.mockMovieRatingRepository, times(1)).save(eq(movieRating));
        verify(this.mockMovieRatingAverageRepository, times(1)).addRating(eq("t1"), eq(9L));
    }

    @SneakyThrows
    private int parseBoxOffice(String boxOffice) {
        return usdFormatter.parse(boxOffice).intValue();
    }
}
