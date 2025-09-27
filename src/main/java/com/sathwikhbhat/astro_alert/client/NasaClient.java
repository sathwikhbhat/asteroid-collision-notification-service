package com.sathwikhbhat.astro_alert.client;

import com.sathwikhbhat.astro_alert.dto.Asteroids;
import com.sathwikhbhat.astro_alert.dto.NasaNeoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@Service
public class NasaClient {

    @Value("${nasa.neo.api.url}")
    private String nasaNeoApiUrl;

    @Value("${nasa.api.key}")
    private String nasaApiKey;

    public List<Asteroids> getNeoAsteroids(LocalDate fromDate, LocalDate toDate) {
        final RestTemplate restTemplate = new RestTemplate();
        final NasaNeoResponse response = restTemplate
                .getForObject(
                        getUrl(fromDate, toDate), NasaNeoResponse.class);

        return response != null ?
                response.getNearEarthObjects()
                        .values()
                        .stream()
                        .flatMap(List::stream)
                        .toList() : List.of();
    }

    public String getUrl(LocalDate fromDate, LocalDate toDate) {
        return UriComponentsBuilder.fromUriString(nasaNeoApiUrl)
                .queryParam("start_date", fromDate)
                .queryParam("end_date", toDate)
                .queryParam("api_key", nasaApiKey)
                .toUriString();
    }

}