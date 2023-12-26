package com.ramnelov.backend.repository;

import com.ramnelov.backend.model.UserEntity;
import com.ramnelov.backend.repository.UserRepository;
import com.ramnelov.backend.utils.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsernameIgnoreCase() {
        UserEntity user = new UserEntity();
        user.setUsername("username");
        user.setPassword("password");
        user.setRole(Role.USER);
        user.setEmail("test@example.com");
        userRepository.save(user);
        assertTrue(userRepository.findByUsernameIgnoreCase("USERNAME").isPresent());
    }

    @Test
    public void testExistsByUsernameIgnoreCase() {
        UserEntity user = new UserEntity();
        user.setUsername("username");
        user.setPassword("password");
        user.setRole(Role.USER);
        user.setEmail("test@example.com");
        userRepository.save(user);
        assertTrue(userRepository.existsByUsernameIgnoreCase("USERNAME"));
    }

    @Test
    public void testFindByEmailIgnoreCase() {
        UserEntity user = new UserEntity();
        user.setUsername("username");
        user.setPassword("password");
        user.setRole(Role.USER);
        user.setEmail("test@example.com");
        userRepository.save(user);
        assertTrue(userRepository.findByEmailIgnoreCase("TEST@EXAMPLE.COM").isPresent());
    }

    @Test
    public void testExistsByEmailIgnoreCase() {
        UserEntity user = new UserEntity();
        user.setUsername("username");
        user.setPassword("password");
        user.setRole(Role.USER);
        user.setEmail("test@example.com");
        userRepository.save(user);
        assertTrue(userRepository.existsByEmailIgnoreCase("TEST@EXAMPLE.COM"));
    }
}
