package com.apsharma.ims_api.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        String jsonResponse = "{\n" +
                "  \"code\": 403,\n" +
                "  \"message\": \"Forbidden cannot access the resource\",\n" +
                "  \"status\": \"Forbidden\",\n" +
                "  \"timestamp\": \"" + LocalDateTime.now() + "\"\n" +
                "}";
        response.getWriter().write(jsonResponse);
    }
}

