package com.serdarormanli.backbase.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import(value = {BestPictureOscarRepositoryImpl.class})
public class BestPictureOscarRepositoryTests {

    @Autowired
    private BestPictureOscarRepository bestPictureOscarRepository;

    @Test
    void winner() {
        var year = this.bestPictureOscarRepository.getYearOfWinner("The Departed");

        assertThat(year).contains("2006 (79th)");
    }

    @Test
    void loser() {
        var year = this.bestPictureOscarRepository.getYearOfWinner("The Green Mile");

        assertThat(year).isEmpty();
    }

    @Test
    void notExists() {
        var year = this.bestPictureOscarRepository.getYearOfWinner("XCV");

        assertThat(year).isEmpty();
    }
}
