package com.serdarormanli.backbase.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record MovieResponse(

        @JsonProperty("Metascore")
        String metascore,

        @JsonProperty("BoxOffice")
        String boxOffice,

        @JsonProperty("Website")
        String website,

        @JsonProperty("imdbRating")
        String imdbRating,

        @JsonProperty("imdbVotes")
        String imdbVotes,

        @JsonProperty("Ratings")
        List<MovieResponseRatingsItem> ratings,

        @JsonProperty("Runtime")
        String runtime,

        @JsonProperty("Language")
        String language,

        @JsonProperty("Rated")
        String rated,

        @JsonProperty("Production")
        String production,

        @JsonProperty("Released")
        String released,

        @JsonProperty("imdbID")
        String imdbID,

        @JsonProperty("Plot")
        String plot,

        @JsonProperty("Director")
        String director,

        @JsonProperty("Title")
        String title,

        @JsonProperty("Actors")
        String actors,

        @JsonProperty("Response")
        String response,

        @JsonProperty("Type")
        String type,

        @JsonProperty("Awards")
        String awards,

        @JsonProperty("DVD")
        String dVD,

        @JsonProperty("Year")
        String year,

        @JsonProperty("Poster")
        String poster,

        @JsonProperty("Country")
        String country,

        @JsonProperty("Genre")
        String genre,

        @JsonProperty("Writer")
        String writer,

        @JsonProperty("Error")
        String error
) {
}