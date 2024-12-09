package com.serdarormanli.backbase.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class RateMovieRequest {
    @Min(1)
    @Max(10)
    private Integer rating;
}
