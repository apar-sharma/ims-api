package com.apsharma.ims_api.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        String jsonResponse = "{\n" +
                "  \"code\": 401,\n" +
                "  \"message\": \"Unauthorized cannot access the resource\",\n" +
                "  \"status\": \"Unauthorized\",\n" +
                "  \"timestamp\": \"" + LocalDateTime.now() + "\"\n" +
                "}";
        response.getWriter().write(jsonResponse);
    }
}

