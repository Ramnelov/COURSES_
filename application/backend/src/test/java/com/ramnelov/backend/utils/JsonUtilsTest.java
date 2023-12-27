package com.ramnelov.backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ramnelov.backend.model.UserEntity;
import com.ramnelov.backend.utils.JsonUtils;
import com.ramnelov.backend.utils.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JsonUtilsTest {

    @Test
    public void testToJson() throws JsonProcessingException {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setRole(Role.USER);

        String actualJson = JsonUtils.toJson(user);

        String expectedJson = "{\"id\":1,\"username\":\"username\",\"password\":\"password\",\"email\":\"test@example.com\",\"role\":\"USER\"}";

        assertEquals(expectedJson, actualJson);
    }
}
