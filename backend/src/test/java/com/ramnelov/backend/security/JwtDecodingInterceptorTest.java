package com.ramnelov.backend.unit.security;

import com.ramnelov.backend.security.JwtDecodingInterceptor;
import com.ramnelov.backend.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtDecodingInterceptorTest {

    @Test
    public void testPreHandle() throws Exception {
        // Mock dependencies
        TokenService tokenService = mock(TokenService.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Jwt jwt = mock(Jwt.class);

        // Define behavior for mocked methods
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(tokenService.decodeToken("token")).thenReturn(jwt);

        // Create instance of class to test
        JwtDecodingInterceptor interceptor = new JwtDecodingInterceptor(tokenService);

        // Call method to test
        boolean result = interceptor.preHandle(request, response, new Object());

        // Verify interactions with mocked objects
        verify(request).setAttribute("jwt", jwt);

        // Assert result
        assertTrue(result);
    }

    @Test
    public void testPreHandleNoToken() throws Exception {
        // Mock dependencies
        TokenService tokenService = mock(TokenService.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Define behavior for mocked methods
        when(request.getHeader("Authorization")).thenReturn(null);

        // Create instance of class to test
        JwtDecodingInterceptor interceptor = new JwtDecodingInterceptor(tokenService);

        // Call method to test
        boolean result = interceptor.preHandle(request, response, new Object());

        // Verify interactions with mocked objects
        verify(request, never()).setAttribute(anyString(), any());

        // Assert result
        assertTrue(result);
    }
}
