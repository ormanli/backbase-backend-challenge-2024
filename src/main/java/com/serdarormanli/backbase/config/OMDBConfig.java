package com.serdarormanli.backbase.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
@Data
public class OMDBConfig {
    @NotBlank
    @Value("${omdb.url}")
    private URI url;

    @NotBlank
    @Value("${omdb.token}")
    private String token;
}
