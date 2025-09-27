package com.sathwikhbhat.astro_alert.service;

import com.sathwikhbhat.astro_alert.client.NasaClient;
import com.sathwikhbhat.astro_alert.dto.Asteroids;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class AsteroidAlertingService {

    @Autowired
    private NasaClient nasaClient;

    public void alert() {
        log.info("Alerting asterid service called");

        final LocalDate fromDate = LocalDate.now();
        final LocalDate toDate = fromDate.plusDays(7);

        log.info("Getting asteroid list from {} to {}", fromDate, toDate);
        List<Asteroids> asteroidList = nasaClient.getNeoAsteroids(fromDate, toDate);
        log.info("Total asteroids received: {}", asteroidList.size());

        List<Asteroids> dangerousAsteroids = asteroidList.stream()
                .filter(Asteroids::isPotentiallyHazardousAsteroid)
                .toList();
        log.info("Found {} hazardous asteroids", dangerousAsteroids.size());
    }
}