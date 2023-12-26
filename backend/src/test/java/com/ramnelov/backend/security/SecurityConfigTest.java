package com.ramnelov.backend.unit.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.ramnelov.backend.security.SecurityConfig;
import com.ramnelov.backend.service.UserDetailsServiceImpl;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SecurityConfigTest {

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private JWKSource<SecurityContext> jwkSource;

    @Mock
    private RSAKey rsaKey;

    @InjectMocks
    private SecurityConfig securityConfig;

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testAuthManager() {
        AuthenticationManager authManager = securityConfig.authManager(userDetailsService);
        assertTrue(authManager instanceof ProviderManager);
    }

    @Test
    public void testUserDetailsService() {
        UserDetailsService result = securityConfig.userDetailsService();
        assertEquals(userDetailsService, result);
    }

    @Test
    public void testBucket() {
        Bucket bucket = securityConfig.bucket();
        assertNotNull(bucket);
    }

    @Test
    public void testJwkSource() {
        ReflectionTestUtils.setField(securityConfig, "rsaKey", rsaKey);
        JWKSource<SecurityContext> result = securityConfig.jwkSource();
        assertNotNull(result);
    }

    @Test
    public void testJwtEncoder() {
        JwtEncoder result = securityConfig.jwtEncoder(jwkSource);
        assertTrue(result instanceof NimbusJwtEncoder);
    }

    @Test
    public void testJwtDecoder() throws JOSEException {
        securityConfig.jwkSource();
        JwtDecoder result = securityConfig.jwtDecoder();
        assertTrue(result instanceof NimbusJwtDecoder);
    }

    @Test
    public void testCorsConfigurationSource() {
        CorsConfigurationSource result = securityConfig.corsConfigurationSource();
        assertNotNull(result);
    }
}
