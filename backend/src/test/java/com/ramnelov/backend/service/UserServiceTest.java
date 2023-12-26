package com.ramnelov.backend.unit.service;

import com.ramnelov.backend.model.UserEntity;
import com.ramnelov.backend.repository.UserRepository;
import com.ramnelov.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testGetAllUsers() {
        List<UserEntity> users = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(users);
        List<UserEntity> result = userService.getAllUsers();
        assertEquals(users, result);
    }

    @Test
    public void testGetUserByUsername() {
        UserEntity user = new UserEntity();
        when(userRepository.findByUsernameIgnoreCase("username")).thenReturn(Optional.of(user));
        UserEntity result = userService.getUserByUsername("username");
        assertEquals(user, result);
    }

    @Test
    public void testGetUserByEmail() {
        UserEntity user = new UserEntity();
        when(userRepository.findByEmailIgnoreCase("email")).thenReturn(Optional.of(user));
        UserEntity result = userService.getUserByEmail("email");
        assertEquals(user, result);
    }

    @Test
    public void testUserExistsByUsername() {
        when(userRepository.existsByUsernameIgnoreCase("username")).thenReturn(true);
        boolean result = userService.userExistsByUsername("username");
        assertTrue(result);
    }

    @Test
    public void testUserExistById() {
        when(userRepository.existsById(1L)).thenReturn(true);
        boolean result = userService.userExistById(1L);
        assertTrue(result);
    }

    @Test
    public void testUserExistsByEmail() {
        when(userRepository.existsByEmailIgnoreCase("email")).thenReturn(true);
        boolean result = userService.userExistsByEmail("email");
        assertTrue(result);
    }

    @Test
    public void testGetUserById() {
        UserEntity user = new UserEntity();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Optional<UserEntity> result = userService.getUserById(1L);
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testCreateUser() {
        UserEntity user = new UserEntity();
        user.setPassword("password");
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        UserEntity result = userService.createUser(user);
        assertEquals("encodedPassword", result.getPassword());
    }

    @Test
    public void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testIsPasswordSafe() {
        assertTrue(userService.isPasswordSafe("Password1!"));
        assertFalse(userService.isPasswordSafe("password"));
    }

    @Test
    public void testIsValidEmail() {
        assertTrue(userService.isValidEmail("test@example.com"));
        assertFalse(userService.isValidEmail("test"));
    }

    @Test
    public void testIsValidUsername() {
        assertTrue(userService.isValidUsername("testUser"));
        assertFalse(userService.isValidUsername("t"));
    }

    @Test
    public void testIsValidRole() {
        assertTrue(userService.isValidRole("USER"));
        assertFalse(userService.isValidRole("INVALID"));
    }
}
