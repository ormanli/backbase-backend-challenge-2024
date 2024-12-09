package com.serdarormanli.backbase.repository;

import com.serdarormanli.backbase.model.MovieRating;
import org.springframework.data.repository.CrudRepository;

public interface MovieRatingRepository extends CrudRepository<MovieRating, Long> {
}
