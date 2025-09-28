package com.sathwikhbhat.astro_alert.service;

import com.sathwikhbhat.astro_alert.client.NasaClient;
import com.sathwikhbhat.astro_alert.dto.Asteroids;
import com.sathwikhbhat.astro_alert.event.AsteroidCollisionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class AsteroidAlertingService {

    @Autowired
    private NasaClient nasaClient;

    @Autowired
    private KafkaTemplate<String, AsteroidCollisionEvent> kafkaTemplate;

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

        List<AsteroidCollisionEvent> asteroidCollisionEvents =
                createEventListOfDangerousAsteroids(dangerousAsteroids);

        log.info("Sending {} asteroid alerts to Kafka", asteroidCollisionEvents.size());
        asteroidCollisionEvents.forEach(event -> {
            kafkaTemplate.send("asteroid-alerts", event);
            log.info("Asteroid alert sent to Kafka Topic: {}", event.getAsteroidName());
        });

    }

    private List<AsteroidCollisionEvent> createEventListOfDangerousAsteroids(List<Asteroids> dangerousAsteroids) {
        return dangerousAsteroids.stream()
                .map(asteroid -> {
                    if (asteroid.isPotentiallyHazardousAsteroid()) {
                        return AsteroidCollisionEvent.builder()
                                .asteroidName(asteroid.getName())
                                .closeApproachDate(asteroid.getCloseApproachData().getFirst().getCloseApproachDate().toString())
                                .missDistanceInKm(asteroid.getCloseApproachData().getFirst().getMissDistance().getKilometers())
                                .estimatedAvgDiameterInMeters(
                                        asteroid.getEstimatedDiameter().getMeters().getMaxDiameter()
                                                + asteroid.getEstimatedDiameter().getMeters().getMinDiameter() / 2)
                                .build();
                    }
                    return null;
                }).toList();
    }

}