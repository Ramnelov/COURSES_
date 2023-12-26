package com.ramnelov.backend.service;

import com.ramnelov.backend.service.TokenService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private JwtDecoder jwtDecoder;

    @Mock
    private Authentication authentication;

    @Test
    public void testDecodeToken() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "username")
                .build();
        when(jwtDecoder.decode("token")).thenReturn(jwt);
        Jwt result = tokenService.decodeToken("token");
        assertEquals(jwt, result);
    }

    @Test
    public void testExtractToken() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "username")
                .build();
        when(jwtDecoder.decode("token")).thenReturn(jwt);
        Jwt result = tokenService.extractToken("Bearer token");
        assertEquals(jwt, result);
    }

    @Test
    public void testGetUsername() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "username")
                .build();
        String result = tokenService.getUsername(jwt);
        assertEquals("username", result);
    }

    @Test
    public void testGetAuthority() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("scope", "ROLE_USER")
                .build();
        String result = tokenService.getAuthority(jwt);
        assertEquals("ROLE_USER", result);
    }

    @Test
    public void testGenerateToken() {
        Collection authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        when(authentication.getAuthorities()).thenReturn(authorities);
        when(authentication.getName()).thenReturn("username"); // Set principal
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "username")
                .claim("scope", "ROLE_USER")
                .build();
        when(jwtEncoder.encode(any())).thenReturn(jwt);
        String result = tokenService.generateToken(authentication);
        assertEquals("token", result);
    }
}