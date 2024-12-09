package com.serdarormanli.backbase.repository;

import java.util.Optional;

public interface BestPictureOscarRepository {
    Optional<String> getYearOfWinner(String movieTitle);
}
