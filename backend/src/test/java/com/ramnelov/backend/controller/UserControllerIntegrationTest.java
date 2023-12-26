package com.ramnelov.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramnelov.backend.dto.UserDTO;
import com.ramnelov.backend.exception.UserNotFoundException;
import com.ramnelov.backend.model.UserEntity;
import com.ramnelov.backend.service.TokenService;
import com.ramnelov.backend.service.UserService;
import com.ramnelov.backend.utils.JsonUtils;
import com.ramnelov.backend.utils.Role;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.management.remote.JMXAuthenticator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Test
    public void testCreateUserAdmin() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("testPassword1!");
        userDTO.setRole("ADMIN");

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "admin",
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ADMIN"))
        );

        String jwt = tokenService.generateToken(authentication);

        mockMvc.perform(post("/api/users/admin")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk());

        userService.deleteUser(userService.getUserByUsername(userDTO.getUsername()).getId());
    }

    @Test
    public void testCreateUser() throws Exception {
        // Create a UserDTO object
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("testPassword1!");

        // Perform the POST request
        mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk());

        userService.deleteUser(userService.getUserByUsername(userDTO.getUsername()).getId());
    }

    @Test
    public void testToken() throws Exception {
        UserEntity user = new UserEntity();
        user.setUsername("testUserToken");
        user.setPassword("testPassword1!");
        user.setEmail("testToken@example.com");
        user.setRole(Role.USER);

        UserEntity userCreated = userService.createUser(user);

        // Create a UserDTO object
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUserToken");
        userDTO.setPassword("testPassword1!");

        mockMvc.perform(post("/api/users/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("eyJraWQi")));

        userService.deleteUser(userCreated.getId());
    }

    @Test
    public void testUpdateUserAdmin() throws Exception {

        // Create a UserDTO object
        UserDTO userToBeUpdated = new UserDTO();
        userToBeUpdated.setUsername("userToBeUpdated");
        userToBeUpdated.setPassword("Password1!");
        userToBeUpdated.setEmail("to.be.updated@example.com");
        userToBeUpdated.setRole("ADMIN");

        UserEntity user = userService.createUser(UserEntity.fromDTO(userToBeUpdated));

        // Create a UserDTO object
        UserDTO userUpdated = new UserDTO();
        userUpdated.setUsername("updatedUsername");
        userUpdated.setPassword("updatedPassword1!");
        userUpdated.setEmail("updated@example.com");
        userUpdated.setRole("USER");

        // Assume that you have a valid user id and admin JWT token
        Long userId = user.getId();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "admin",
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ADMIN"))
        );

        String jwt = tokenService.generateToken(authentication);


        // Perform the PUT request
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/users/admin/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(userUpdated))
                        .header("Authorization", "Bearer " + jwt))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(content, "User updated");

        // Verify that the user was updated
        UserEntity updatedUser = userService.getUserById(userId).orElseThrow(RuntimeException::new);
        assertEquals("updatedUsername", updatedUser.getUsername());
        assertEquals(userService.encodePassword(userUpdated.getPassword()).substring(0, 7), updatedUser.getPassword().substring(0, 7));
        assertEquals("updated@example.com", updatedUser.getEmail());
        assertEquals("USER", updatedUser.getRole().name());

        userService.deleteUser(userId);
    }

    @Test
    public void testUpdateUser() throws Exception {

        // Create a UserDTO object
        UserDTO userToBeUpdated = new UserDTO();
        userToBeUpdated.setUsername("userToBeUpdated");
        userToBeUpdated.setPassword("Password1!");
        userToBeUpdated.setEmail("to.be.updated@example.com");
        userToBeUpdated.setRole("USER");

        UserEntity user = userService.createUser(UserEntity.fromDTO(userToBeUpdated));

        // Create a UserDTO object
        UserDTO userUpdated = new UserDTO();
        userUpdated.setUsername("updatedUsername");
        userUpdated.setPassword("updatedPassword1!");
        userUpdated.setEmail("updated@example.com");

        // Assume that you have a valid user id and admin JWT token
        Long userId = user.getId();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userToBeUpdated.getUsername(),
                userToBeUpdated.getPassword()
        );

        String jwt = tokenService.generateToken(authentication);


        // Perform the PUT request
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(userUpdated))
                        .header("Authorization", "Bearer " + jwt))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(content, "User updated");

        // Verify that the user was updated
        UserEntity updatedUser = userService.getUserById(userId).orElseThrow(RuntimeException::new);
        assertEquals("updatedUsername", updatedUser.getUsername());
        assertEquals(userService.encodePassword(userUpdated.getPassword()).substring(0, 7), updatedUser.getPassword().substring(0, 7));
        assertEquals("updated@example.com", updatedUser.getEmail());
        assertEquals("USER", updatedUser.getRole().name());

        userService.deleteUser(userId);
    }

    @Test
    public void testDeleteUser() throws Exception {
        // Assume that you have a valid user JWT token

        UserDTO userToBeDeleted = new UserDTO();
        userToBeDeleted.setUsername("userToBeDeleted");
        userToBeDeleted.setPassword("Password1!");
        userToBeDeleted.setEmail("to.be.deleted@example.com");
        userToBeDeleted.setRole("USER");

        userService.createUser(UserEntity.fromDTO(userToBeDeleted));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userToBeDeleted.getUsername(),
                userToBeDeleted.getPassword()
                );

        String userJwt = tokenService.generateToken(authentication);


        // Perform the DELETE request
        mockMvc.perform(delete("/api/users/")
                        .header("Authorization", "Bearer " + userJwt))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted"));


        assertThrowsExactly(UserNotFoundException.class, () -> userService.getUserByUsername(userToBeDeleted.getUsername()));
    }

    @Test
    public void testDeleteUserAdmin() throws Exception {
        // Assume that you have a valid user JWT token

        UserDTO userToBeDeleted = new UserDTO();
        userToBeDeleted.setUsername("userToBeDeleted");
        userToBeDeleted.setPassword("Password1!");
        userToBeDeleted.setEmail("to.be.deleted@example.com");
        userToBeDeleted.setRole("USER");

        UserEntity user = userService.createUser(UserEntity.fromDTO(userToBeDeleted));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "admin",
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ADMIN"))
        );

        String jwt = tokenService.generateToken(authentication);

        // Perform the DELETE request
        mockMvc.perform(delete("/api/users/admin/" + user.getId())
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted"));


        assertThrowsExactly(UserNotFoundException.class, () -> userService.getUserByUsername(userToBeDeleted.getUsername()));
    }

    @Test
    public void testGetUser() throws Exception {
        // Assume that you have a valid user JWT token

        UserDTO testUser = new UserDTO();
        testUser.setUsername("testUser");
        testUser.setPassword("Password1!");
        testUser.setEmail("testuser@example.com");
        testUser.setRole("USER");

        userService.createUser(UserEntity.fromDTO(testUser));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                testUser.getUsername(),
                testUser.getPassword()
        );

        String jwt = tokenService.generateToken(authentication);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/")
                        .header("Authorization", "Bearer " + jwt))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        UserDTO user = objectMapper.readValue(content, UserDTO.class);

        assertEquals(testUser.getUsername(), user.getUsername());
        assertEquals(testUser.getEmail(), user.getEmail());
        assertEquals(testUser.getRole(), user.getRole());
        assertEquals(userService.encodePassword(testUser.getPassword()).substring(0, 7), user.getPassword().substring(0, 7));

        userService.deleteUser(userService.getUserByUsername(testUser.getUsername()).getId());
    }

    @Test
    public void testGetUserAdmin() throws Exception {
        // Assume that you have a valid user JWT token

        UserDTO testUser1 = new UserDTO();
        testUser1.setUsername("testUser1");
        testUser1.setPassword("Password1!");
        testUser1.setEmail("testuser1@example.com");
        testUser1.setRole("USER");

        userService.createUser(UserEntity.fromDTO(testUser1));

        UserDTO testUser2 = new UserDTO();
        testUser2.setUsername("testUser2");
        testUser2.setPassword("Password1!");
        testUser2.setEmail("testuser2@example.com");
        testUser2.setRole("USER");

        userService.createUser(UserEntity.fromDTO(testUser2));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "admin",
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ADMIN"))
        );

        String jwt = tokenService.generateToken(authentication);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/admin")
                        .header("Authorization", "Bearer " + jwt))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        List<UserDTO> users = (new ObjectMapper().readValue(content, new TypeReference<>() {}))
                ;

        List<UserDTO> testUsers = users.stream()
                .filter(user -> user.getUsername().equals("testUser1") || user.getUsername().equals("testUser2"))
                .toList();

        assertEquals(2, testUsers.size());

        assertEquals(testUser1.getUsername(), testUsers.get(0).getUsername());
        assertEquals(testUser1.getEmail(), testUsers.get(0).getEmail());
        assertEquals(testUser1.getRole(), testUsers.get(0).getRole());
        assertEquals(userService.encodePassword(testUser1.getPassword()).substring(0, 7), testUsers.get(0).getPassword().substring(0, 7));

        assertEquals(testUser2.getUsername(), testUsers.get(1).getUsername());
        assertEquals(testUser2.getEmail(), testUsers.get(1).getEmail());
        assertEquals(testUser2.getRole(), testUsers.get(1).getRole());
        assertEquals(userService.encodePassword(testUser2.getPassword()).substring(0, 7), testUsers.get(1).getPassword().substring(0, 7));

        userService.deleteUser(userService.getUserByUsername(testUser1.getUsername()).getId());
        userService.deleteUser(userService.getUserByUsername(testUser2.getUsername()).getId());


    }
}