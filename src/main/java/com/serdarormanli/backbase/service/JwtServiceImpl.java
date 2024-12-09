package com.serdarormanli.backbase.service;

import com.serdarormanli.backbase.config.JWTConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Clock;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class JwtServiceImpl implements JwtService {

    private final JWTConfig jwtConfig;
    private final Clock clock;

    @Override
    public String extractUserName(@NonNull @NotBlank String token) {
        return this.extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generateToken(@NonNull UserDetails userDetails) {
        return this.generateToken(new HashMap<>(), userDetails);
    }

    @Override
    public boolean isTokenValid(@NonNull @NotBlank String token, @NonNull UserDetails userDetails) {
        var userName = this.extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !this.isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        var claims = this.extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(this.clock.instant()))
                .setExpiration(Date.from(this.clock.instant().plus(1, ChronoUnit.DAYS)))
                .signWith(this.getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return this.extractExpiration(token).before(Date.from(this.clock.instant()));
    }

    private Date extractExpiration(String token) {
        return this.extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(this.getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Key getSigningKey() {
        var keyBytes = Decoders.BASE64.decode(this.jwtConfig.getJwtSigningKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
