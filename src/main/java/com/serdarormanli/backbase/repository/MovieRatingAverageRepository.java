package com.serdarormanli.backbase.repository;

import com.serdarormanli.backbase.model.MovieRatingAverage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRatingAverageRepository extends CrudRepository<MovieRatingAverage, String> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "INSERT INTO movie_rating_average (imdb_id, rating_sum, rating_count)" +
            " VALUES (:imdbId, :rating, 1)" +
            " ON DUPLICATE KEY UPDATE" +
            " rating_sum = rating_sum + :rating," +
            " rating_count = rating_count + 1", nativeQuery = true)
    void addRating(@Param("imdbId") String imdbId, @Param("rating") long rating);

    @Query("SELECT mra FROM MovieRatingAverage mra ORDER BY mra.ratingAverage DESC LIMIT ?1")
    List<MovieRatingAverage> findTopKRatedMovies(Integer k);
}
