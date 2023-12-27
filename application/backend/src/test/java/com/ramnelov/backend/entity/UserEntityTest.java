package com.ramnelov.backend.entity;

import com.ramnelov.backend.dto.UserDTO;
import com.ramnelov.backend.model.UserEntity;
import com.ramnelov.backend.utils.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

public class UserEntityTest {

    @Test
    public void testUserEntity() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setRole(Role.USER);

        assertEquals(1L, user.getId());
        assertEquals("username", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
        assertEquals(Role.USER, user.getRole());
    }

    @Test
    public void testFromDTO() {
        UserDTO dto = new UserDTO();
        dto.setUsername("username");
        dto.setPassword("password");
        dto.setEmail("test@example.com");
        dto.setRole("USER");

        UserEntity user = UserEntity.fromDTO(dto);

        assertEquals(dto.getUsername(), user.getUsername());
        assertEquals(dto.getPassword(), user.getPassword());
        assertEquals(dto.getEmail(), user.getEmail());
        assertEquals(dto.getRole(), user.getRole().name());
    }

    @Test
    public void testToDTO() {
        UserEntity user = new UserEntity();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setRole(Role.USER);

        UserDTO dto = UserEntity.toDTO(user);

        assertEquals(user.getUsername(), dto.getUsername());
        assertEquals(user.getPassword(), dto.getPassword());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getRole().name(), dto.getRole());
    }

    @Test
    public void testToSpringSecurityUser() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setRole(Role.USER);

        UserDetails userDetails = user.toSpringSecurityUser();

        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(user.getRole().name())));
    }
}
