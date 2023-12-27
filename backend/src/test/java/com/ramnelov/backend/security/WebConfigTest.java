package com.ramnelov.backend.security;

import com.ramnelov.backend.security.JwtDecodingInterceptor;
import com.ramnelov.backend.security.WebConfig;
import com.ramnelov.backend.service.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WebConfigTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private InterceptorRegistry interceptorRegistry;

    @InjectMocks
    private WebConfig webConfig;

    @Captor
    private ArgumentCaptor<JwtDecodingInterceptor> argumentCaptor;

    @Test
    public void testAddInterceptors() {
        webConfig.addInterceptors(interceptorRegistry);
        verify(interceptorRegistry).addInterceptor(argumentCaptor.capture());
        JwtDecodingInterceptor interceptor = argumentCaptor.getValue();
        assertEquals(tokenService, interceptor.getTokenService());
    }
}