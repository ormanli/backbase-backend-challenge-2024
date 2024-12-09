package com.serdarormanli.backbase.repository;

import com.serdarormanli.backbase.model.MovieRating;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ActiveProfiles("test")
@TestConfiguration(proxyBeanMethods = false)
@SpringBootTest(webEnvironment = NONE)
@Testcontainers(disabledWithoutDocker = true)
@Transactional
public class MovieRatingRepositoryTests {
    @Container
    private static final MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:9"))
            .withInitScript("init.sql")
            .withDatabaseName("backbase");

    private final Instant now = Instant.now().minus(5, ChronoUnit.MINUTES);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    private MovieRatingRepository movieRatingRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void init() {
        new SimpleJdbcInsert(this.jdbcTemplate).
                withTableName("movie_rating").
                execute(
                        Map.of(
                                "id", 1,
                                "imdb_id", "t1",
                                "rating", 10,
                                "created_at", LocalDateTime.ofInstant(now, ZoneOffset.UTC),
                                "api_user_id", 1
                        )
                );
    }

    @AfterEach
    public void cleanup() {
        this.jdbcTemplate.execute("TRUNCATE TABLE movie_rating");
    }

    @Test
    void insertAnotherRatingToExistingMovie() {
        var movieRating = new MovieRating();
        movieRating.setImdbId("t1");
        movieRating.setRating(10);
        movieRating.setCreatedAt(now);
        movieRating.setApiUserId(1L);

        assertThatThrownBy(() -> this.movieRatingRepository.save(movieRating))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Duplicate entry 't1-1' for key 'movie_rating.movie_rating_imdb_id_api_user_id_ukey'");
    }

    @Test
    void insertRatingToAnotherMovie() {
        var movieRating = new MovieRating();
        movieRating.setImdbId("t2");
        movieRating.setRating(10);
        movieRating.setCreatedAt(now);
        movieRating.setApiUserId(1L);

        var saved = this.movieRatingRepository.save(movieRating);
        assertThat(this.movieRatingRepository.findById(saved.getId())).containsSame(saved);
    }

}
