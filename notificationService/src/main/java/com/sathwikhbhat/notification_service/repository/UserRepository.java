package com.sathwikhbhat.notification_service.repository;

import com.sathwikhbhat.notification_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}