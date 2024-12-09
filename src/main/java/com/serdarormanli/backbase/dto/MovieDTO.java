package com.serdarormanli.backbase.dto;

import java.math.BigDecimal;

public record MovieDTO(String title, String imdbId, BigDecimal averageRating, String boxOfficeValue) {
}
