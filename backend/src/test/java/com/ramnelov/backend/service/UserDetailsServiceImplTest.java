package com.ramnelov.backend.unit.service;

import com.ramnelov.backend.model.UserEntity;
import com.ramnelov.backend.repository.UserRepository;
import com.ramnelov.backend.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testLoadUserByUsername() {
        UserEntity user = new UserEntity();
        user.setUsername("username");
        user.setPassword("password");
        when(userRepository.findByUsernameIgnoreCase("username")).thenReturn(Optional.of(user));
        UserDetails result = userDetailsService.loadUserByUsername("username");
        assertEquals(user.getUsername(), result.getUsername());
    }

    @Test
    public void testLoadUserByUsernameNotFound() {
        when(userRepository.findByUsernameIgnoreCase("username")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("username");
        });
    }
}