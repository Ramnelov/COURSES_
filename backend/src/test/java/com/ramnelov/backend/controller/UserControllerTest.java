package com.ramnelov.backend.unit.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramnelov.backend.controller.UserController;
import com.ramnelov.backend.dto.UserDTO;
import com.ramnelov.backend.model.UserEntity;
import com.ramnelov.backend.service.TokenService;
import com.ramnelov.backend.service.UserService;
import com.ramnelov.backend.utils.JsonUtils;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.ramnelov.backend.utils.Role;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private TokenService tokenService;

    @Mock
    private Bucket bucket;

    @Mock
    private AuthenticationManager authenticationManager;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();



        ReflectionTestUtils.setField(userController, "bucket", bucket);

        lenient().when(userService.userExistsByUsername(any())).thenReturn(false);
        lenient().when(userService.userExistsByEmail(any())).thenReturn(false);

        lenient().when(userService.isValidUsername(any())).thenReturn(true);
        lenient().when(userService.isValidEmail(any())).thenReturn(true);
        lenient().when(userService.isPasswordSafe(any())).thenReturn(true);
        lenient().when(userService.isValidRole(any())).thenReturn(true);
        lenient().when(userService.encodePassword(any())).thenReturn("EncodedPassword1!");
        lenient().when(tokenService.getAuthority(any())).thenReturn("ADMIN");
        lenient().when(tokenService.getUsername(any())).thenReturn("testUser");
        lenient().when(bucket.tryConsume(anyLong())).thenReturn(true);

    }

    @Test
    public void testCreateUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        userDTO.setEmail("test@test.com");
        userDTO.setPassword("Password1!");
        userDTO.setRole("USER");


        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(userDTO)))
                        .andExpect(status().isOk());
    }

    @Test
    public void testCreateUserAdmin() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("adminTest");
        userDTO.setEmail("adminTest@test.com");
        userDTO.setPassword("Password1!");
        userDTO.setRole("ADMIN");

        Map<String, Object> claims = new HashMap<>();
        claims.put("authority", "ADMIN");
        claims.put("username", "testUser");

        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");

        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(3600), headers, claims);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(userDTO))
                        .requestAttr("jwt", jwt))
                .andExpect(status().isOk());
    }

    @Test
    public void testToken() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        userDTO.setPassword("Password1!");

        when(bucket.tryConsume(anyLong())).thenReturn(true);

        Authentication auth = new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

        when(tokenService.generateToken(any())).thenReturn("token");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(userDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("token", content);
    }

    @Test
    public void testUpdateUserAdmin() throws Exception {



        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("updatedUser");
        userDTO.setPassword("UpdatedPassword1!");
        userDTO.setEmail("updatedUser@test.com");
        userDTO.setRole("USER");

        Map<String, Object> claims = new HashMap<>();
        claims.put("authority", "ADMIN");
        claims.put("username", "testUser");

        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");

        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(3600), headers, claims);

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("Password1!");
        user.setEmail("testUser@test.com");
        user.setRole(Role.USER);

        lenient().when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        lenient().when(userService.userExistById(1L)).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/users/admin/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("jwt", jwt)
                        .content(JsonUtils.toJson(userDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("User updated", content);

    }

    @Test
    public void testUpdateUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("updatedTestUser");
        userDTO.setEmail("updatedTestUser@test.com");
        userDTO.setPassword("UpdatedPassword1!");

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("EncodedPassword1!");
        user.setEmail("testUser@test.com");
        user.setRole(Role.USER);

        Map<String, Object> claims = new HashMap<>();
        claims.put("authority", "USER");
        claims.put("username", "testUser");

        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");

        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(3600), headers, claims);

        lenient().when(userService.getUserByUsername("testUser")).thenReturn(user);
        lenient().when(userService.userExistsByUsername(tokenService.getUsername(jwt))).thenReturn(true);
        lenient().when(userService.userExistsByUsername(userDTO.getUsername())).thenReturn(false);
        lenient().when(userService.isValidUsername(userDTO.getUsername())).thenReturn(true);
        lenient().when(userService.isPasswordSafe(userDTO.getPassword())).thenReturn(true);
        lenient().when(userService.userExistsByEmail(userDTO.getEmail())).thenReturn(false);
        lenient().when(userService.isValidEmail(userDTO.getEmail())).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("jwt", jwt)
                        .content(JsonUtils.toJson(userDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("User updated", content);
    }

    @Test
    public void testDeleteUser() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("EncodedPassword1!");
        user.setEmail("testUser@test.com");
        user.setRole(Role.USER);

        Map<String, Object> claims = new HashMap<>();
        claims.put("authority", "USER");
        claims.put("username", "testUser");

        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");

        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(3600), headers, claims);

        lenient().when(userService.getUserByUsername("testUser")).thenReturn(user);
        lenient().when(userService.userExistsByUsername(tokenService.getUsername(jwt))).thenReturn(true);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("jwt", jwt))
                .andExpect(status().isOk())
                .andReturn();


        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("User deleted", content);
    }

    @Test
    public void testDeleteUserAdmin() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("EncodedPassword1!");
        user.setEmail("testUser@test.com");
        user.setRole(Role.USER);

        Map<String, Object> claims = new HashMap<>();
        claims.put("authority", "ADMIN");
        claims.put("username", "testUser");

        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");

        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(3600), headers, claims);

        lenient().when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        lenient().when(userService.userExistById(1L)).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/admin/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("jwt", jwt))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("User deleted", content);
    }

    @Test
    public void testGetUser() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("EncodedPassword1!");
        user.setEmail("testUser@test.com");
        user.setRole(Role.USER);

        Map<String, Object> claims = new HashMap<>();
        claims.put("authority", "USER");
        claims.put("username", "testUser");

        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");

        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(3600), headers, claims);

        lenient().when(userService.getUserByUsername("testUser")).thenReturn(user);
        lenient().when(userService.userExistsByUsername(tokenService.getUsername(jwt))).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("jwt", jwt))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        UserDTO userDTO = new ObjectMapper().readValue(content, UserDTO.class);

        assertEquals(user.getUsername(), userDTO.getUsername());
    }

    @Test
    public void testGetUserAdmin() throws Exception {
        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        user1.setUsername("testUser1");
        user1.setPassword("EncodedPassword1!");
        user1.setEmail("testUser1@test.com");
        user1.setRole(Role.USER);

        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        user2.setUsername("testUser2");
        user2.setPassword("EncodedPassword2!");
        user2.setEmail("testUser2@test.com");
        user2.setRole(Role.USER);

        List<UserEntity> users = Arrays.asList(user1, user2);

        Map<String, Object> claims = new HashMap<>();
        claims.put("authority", "ADMIN");
        claims.put("username", "adminUser");

        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");

        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(3600), headers, claims);

        lenient().when(userService.getAllUsers()).thenReturn(users);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("jwt", jwt))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        List<UserDTO> userDTOs = new ObjectMapper().readValue(content, new TypeReference<List<UserDTO>>(){});

        assertEquals(users.size(), userDTOs.size());
        assertEquals(user1.getUsername(), userDTOs.get(0).getUsername());
        assertEquals(user2.getUsername(), userDTOs.get(1).getUsername());
    }
}