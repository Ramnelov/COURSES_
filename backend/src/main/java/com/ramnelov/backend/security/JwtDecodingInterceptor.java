package com.ramnelov.backend.security;

import com.ramnelov.backend.controller.UserController;
import com.ramnelov.backend.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.servlet.HandlerInterceptor;

public class JwtDecodingInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;


    public JwtDecodingInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }


    @Override
    public boolean preHandle(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            Jwt jwt = tokenService.decodeToken(token);
            request.setAttribute("jwt", jwt);
        }

        return true;
    }

    public TokenService getTokenService() {

        return tokenService;
    }
}
