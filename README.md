# Asteroid Collision Notification Service

A microservices-based system that monitors potentially hazardous asteroids using NASA's NEO API and sends email notifications to users. Uses Apache Kafka for event-driven communication between services.

## Overview

Two Spring Boot services:
- **Astro Alert Service** - Fetches asteroid data from NASA API and publishes events to Kafka
- **Notification Service** - Consumes events from Kafka and sends email notifications

## How It Works

<p align="center">
  <img src="https://github.com/user-attachments/assets/d8cce327-b205-4191-bcea-9212a4b53413" alt="Screenshot (37)" width="60%"/>
</p>

**Workflow Steps:**
1. **Data Fetching**: Astro Alert Service calls NASA's NEO API to get asteroid data
2. **Event Publishing**: If hazardous asteroids are found, events are published to Kafka
3. **Event Consumption**: Notification Service consumes events from Kafka
4. **Database Storage**: Notification details are stored in MySQL database
5. **Email Delivery**: Email alerts are sent to all registered users
6. **Scheduled Processing**: Notification Service runs periodic checks for unsent notifications

## Tech Stack

- Java 21
- Spring Boot 3.5.6
- Apache Kafka
- MySQL 8.3.0
- Docker Compose

## Setup

### Prerequisites
- Java 21
- Docker & Docker Compose
- [NASA API Key](https://api.nasa.gov) 
- Gmail App Password

### Installation

1. Clone the repository
```bash
git clone https://github.com/sathwikhbhat/asteroid-collision-notification-service.git
cd asteroid-collision-notification-service
```

2. Start infrastructure services
```bash
cd "Asteroid Alerting"
docker-compose up -d
```

3. Set environment variables
```bash
NASA_API_KEY="your_nasa_api_key"
GMAIL_ID="your@email.com"
GMAIL_APP_PASSWORD="your_app_password"
```

4. Run the services
```bash
# Terminal 1 - Astro Alert Service
cd "Asteroid Alerting"
./mvnw spring-boot:run
```
```bash
# Terminal 2 - Notification Service
cd "Notification Service"
./mvnw spring-boot:run
```

## Usage

### Trigger Asteroid Monitoring
```bash
curl -X POST http://localhost:8080/api/v1/asteroid-alerting/alert
```

### Manage Users
Add a user to receive notifications:
```bash
curl -X POST http://localhost:8081/api/users/add \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Your Name",
    "email": "your-email@example.com",
    "notificationEnabled": true
  }'
```

Get all registered users:
```bash
curl -X GET http://localhost:8081/api/users
```

## API Endpoints

### Astro Alert Service (Port 8080)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/asteroid-alerting/alert` | Triggers asteroid monitoring and alert processing |

### Notification Service (Port 8081)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users/add` | Add a new user to receive notifications |
| GET | `/api/users` | Get all registered users |

## Services

### Astro Alert Service (Port 8080)
- Fetches data from NASA NEO API
- Publishes events to Kafka topic
- Provides REST endpoint for triggering alerts

### Notification Service (Port 8081) 
- Consumes events from Kafka
- Stores notifications in MySQL
- Sends emails to registered users
- Provides REST endpoints for user management

## Monitoring

- Kafka UI: http://localhost:8084

## Acknowledgments

This project was inspired by and references the [LeetJourney Asteroid Notifications](https://github.com/leetjourney/leetjourney-springboot-asteroid-notifications) project. Special thanks to NASA for providing the Near Earth Object API.
