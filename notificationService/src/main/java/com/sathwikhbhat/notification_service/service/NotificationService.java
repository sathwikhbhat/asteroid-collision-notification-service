package com.sathwikhbhat.notification_service.service;

import com.sathwikhbhat.astro_alert.event.AsteroidCollisionEvent;
import com.sathwikhbhat.notification_service.entity.Notification;
import com.sathwikhbhat.notification_service.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Slf4j
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    EmailService emailService;

    @KafkaListener(topics = "asteroid-alert", groupId = "notification-service")
    public void alertEvent(AsteroidCollisionEvent notificationEvent) {
        log.info("Received Asteroid alerting event: {}", notificationEvent);

        Notification notification = Notification.builder()
                .asteroidName(notificationEvent.getAsteroidName())
                .closeApproachDate(LocalDate.parse(notificationEvent.getCloseApproachDate()))
                .estimatedDiameterAvgMeters(notificationEvent.getEstimatedAvgDiameterInMeters())
                .missDistanceKilometers(new BigDecimal(notificationEvent.getMissDistanceInKm()))
                .emailSent(false)
                .build();

        Notification savedNotification = notificationRepository.saveAndFlush(notification);
        log.info("Notification saved: {}", savedNotification);
    }

    @Scheduled(fixedRate = 10000)
    public void sendAlertingEmail() {
        log.info("Triggering scheduled job to send email alerts");
        emailService.sendAsteroidAlertEmail();
    }

}