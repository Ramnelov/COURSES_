package com.ramnelov.backend.service;

import com.ramnelov.backend.exception.UserNotFoundException;
import com.ramnelov.backend.model.UserEntity;
import com.ramnelov.backend.repository.UserRepository;
import org.apache.commons.validator.routines.RegexValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ramnelov.backend.utils.Role;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

    }

    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

    }

    public boolean userExistsByUsername(String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }


    public boolean userExistById(Long id) {
        return userRepository.existsById(id);
    }

    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    public Optional<UserEntity> getUserById(Long userId) {
        return userRepository.findById(userId);
    }


    public UserEntity createUser(UserEntity user) {

        user.setPassword(encodePassword(user.getPassword()));

        return userRepository.save(user);
    }

    public UserEntity updateUser(UserEntity user) {

        user.setPassword(encodePassword(user.getPassword()));

        return userRepository.save(user);
    }

    public String encodePassword(String password){
        return passwordEncoder.encode(password);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }


    public boolean isPasswordSafe(String password) {
        // Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character
        RegexValidator validator = new RegexValidator("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
        return validator.isValid(password);
    }

    public boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        RegexValidator validator = new RegexValidator(regex);
        return validator.isValid(email);
    }

    public boolean isValidUsername(String username) {
        String regex = "^[A-Za-z0-9]{3,20}$";
        RegexValidator validator = new RegexValidator(regex);
        return validator.isValid(username);
    }

    public boolean isValidRole(String role) {
        for (Role r : Role.values()) {
            if (r.name().equals(role)) {
                return true;
            }
        }
        return false;
    }

}