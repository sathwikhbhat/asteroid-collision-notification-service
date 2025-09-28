package com.sathwikhbhat.notification_service.service;

import com.sathwikhbhat.notification_service.entity.User;
import com.sathwikhbhat.notification_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return (!userRepository.existsByEmail(user.getEmail())) ? userRepository.save(user) : null;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}