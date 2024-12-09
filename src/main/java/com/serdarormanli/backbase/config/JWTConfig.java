package com.serdarormanli.backbase.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class JWTConfig {
    @Value("${token.signing.key}")
    private String jwtSigningKey;
}
