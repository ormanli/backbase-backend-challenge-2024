package com.serdarormanli.backbase.repository;


import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.dataformat.csv.CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE;
import static org.springframework.core.io.support.ResourcePatternUtils.getResourcePatternResolver;

@Repository
public class BestPictureOscarRepositoryImpl implements BestPictureOscarRepository {

    private final Map<String, String> bestPictureOscarWinners;

    @SneakyThrows
    public BestPictureOscarRepositoryImpl(@NonNull ResourceLoader resourceLoader) {
        var resourcePatternResolver = getResourcePatternResolver(resourceLoader);

        var csvResources = resourcePatternResolver.getResources("classpath*:academy_awards.csv");

        var mapper = new CsvMapper();
        mapper.enable(IGNORE_TRAILING_UNMAPPABLE);
        mapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);

        var bestPictureOscarWinners = new HashMap<String, String>();

        try (var is = csvResources[0].getInputStream();
             var reader = new InputStreamReader(is, StandardCharsets.ISO_8859_1)) {

            MappingIterator<Map<String, String>> it = mapper
                    .readerFor(Map.class)
                    .with(CsvSchema.emptySchema().withHeader())
                    .readValues(reader);

            while (it.hasNext()) {
                var row = it.next();
                if ("YES".equals(row.get("Won?")) && "Best Picture".equals(row.get("Category"))) {
                    bestPictureOscarWinners.put(row.get("Nominee"), row.get("Year"));
                }
            }
        }

        this.bestPictureOscarWinners = bestPictureOscarWinners;
    }

    @Override
    public Optional<String> getYearOfWinner(@NonNull @NotBlank String movieTitle) {
        return Optional.ofNullable(bestPictureOscarWinners.get(movieTitle));
    }
}
