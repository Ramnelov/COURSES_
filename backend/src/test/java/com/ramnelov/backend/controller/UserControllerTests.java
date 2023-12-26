import com.ramnelov.backend.controller.UserController;
import com.ramnelov.backend.dto.UserDTO;
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
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        lenient().when(userService.encodePassword(any())).thenReturn("EncodedPassword1!")
        lenient().when(tokenService.getAuthority(any())).thenReturn("ADMIN");
        lenient().when(bucket.tryConsume(anyLong())).thenReturn(true);
    }

    @Test
    public void testCreateUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        userDTO.setEmail("test@test.com");
        userDTO.setPassword("Password1!");
        userDTO.setRole("USER");

        mockMvc.perform(post("/api/users/")
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

        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");

        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(3600), headers, claims);

        mockMvc.perform(post("/api/users/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(userDTO))
                        .requestAttr("jwt", jwt))
                .andExpect(status().isOk());
    }
}