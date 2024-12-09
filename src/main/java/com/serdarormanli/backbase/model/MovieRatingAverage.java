package com.serdarormanli.backbase.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "movie_rating_average")
@Data
public class MovieRatingAverage {
    @Id
    private String imdbId;
    private Long ratingSum;
    private Integer ratingCount;
    private BigDecimal ratingAverage;
}
