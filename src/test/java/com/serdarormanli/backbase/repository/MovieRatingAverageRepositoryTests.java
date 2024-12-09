package com.serdarormanli.backbase.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ActiveProfiles("test")
@TestConfiguration(proxyBeanMethods = false)
@SpringBootTest(webEnvironment = NONE)
@Testcontainers(disabledWithoutDocker = true)
@Transactional
public class MovieRatingAverageRepositoryTests {

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
    private MovieRatingAverageRepository movieRatingAverageRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    public void cleanup() {
        this.jdbcTemplate.execute("TRUNCATE TABLE movie_rating_average");
    }

    @Test
    void addRatingToNewMovie() {
        this.movieRatingAverageRepository.addRating("t1", 2);

        var topRatedMovies = this.movieRatingAverageRepository.findTopKRatedMovies(2);

        assertThat(topRatedMovies).hasSize(1);
        assertThat(topRatedMovies.getFirst().getRatingAverage()).isEqualByComparingTo(new BigDecimal(2));
        assertThat(topRatedMovies.getFirst().getImdbId()).isEqualTo("t1");
        assertThat(topRatedMovies.getFirst().getRatingSum()).isEqualTo(2);
        assertThat(topRatedMovies.getFirst().getRatingCount()).isEqualTo(1);
    }

    @Test
    void addRatingToExistingMovie() {
        this.movieRatingAverageRepository.addRating("t1", 2);
        this.movieRatingAverageRepository.addRating("t1", 10);
        this.movieRatingAverageRepository.addRating("t1", 3);

        var topRatedMovies = this.movieRatingAverageRepository.findTopKRatedMovies(2);

        assertThat(topRatedMovies).hasSize(1);
        assertThat(topRatedMovies.getFirst().getRatingAverage()).isEqualByComparingTo(new BigDecimal(5));
        assertThat(topRatedMovies.getFirst().getImdbId()).isEqualTo("t1");
        assertThat(topRatedMovies.getFirst().getRatingSum()).isEqualTo(15);
        assertThat(topRatedMovies.getFirst().getRatingCount()).isEqualTo(3);
    }

    @Test
    void addRatingToMultipleMovies() {
        this.movieRatingAverageRepository.addRating("t1", 2);
        this.movieRatingAverageRepository.addRating("t1", 10);
        this.movieRatingAverageRepository.addRating("t1", 3);

        this.movieRatingAverageRepository.addRating("t2", 10);
        this.movieRatingAverageRepository.addRating("t2", 10);
        this.movieRatingAverageRepository.addRating("t2", 7);
        this.movieRatingAverageRepository.addRating("t2", 9);

        this.movieRatingAverageRepository.addRating("t3", 1);
        this.movieRatingAverageRepository.addRating("t3", 1);

        var topRatedMovies = this.movieRatingAverageRepository.findTopKRatedMovies(2);

        assertThat(topRatedMovies).hasSize(2);

        assertThat(topRatedMovies.getFirst().getRatingAverage()).isEqualByComparingTo(new BigDecimal(9));
        assertThat(topRatedMovies.getFirst().getImdbId()).isEqualTo("t2");
        assertThat(topRatedMovies.getFirst().getRatingSum()).isEqualTo(36);
        assertThat(topRatedMovies.getFirst().getRatingCount()).isEqualTo(4);

        assertThat(topRatedMovies.getLast().getRatingAverage()).isEqualByComparingTo(new BigDecimal(5));
        assertThat(topRatedMovies.getLast().getImdbId()).isEqualTo("t1");
        assertThat(topRatedMovies.getLast().getRatingSum()).isEqualTo(15);
        assertThat(topRatedMovies.getLast().getRatingCount()).isEqualTo(3);
    }
}
