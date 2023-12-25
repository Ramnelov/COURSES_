package com.ramnelov.backend.service;

import com.ramnelov.backend.model.UserEntity;
import com.ramnelov.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.apache.commons.validator.routines.RegexValidator;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserEntity getUserByUsername(String username) {
        // Fetch user from the database
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

    }

    public Optional<UserEntity> getUserById(Long userId) {
        return userRepository.findById(userId);
    }


    public UserEntity createUser(UserEntity user) {


        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public void updateUser(UserEntity user) {


        user.setPassword(passwordEncoder.encode(user.getPassword()));


        userRepository.save(user);
    }


    public boolean isPasswordSafe(String password) {
        // Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character
        RegexValidator validator = new RegexValidator("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
        return validator.isValid(password);
    }



}