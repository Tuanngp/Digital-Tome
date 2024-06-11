package com.fpt.swp391.group6.DigitalTome.config;

import com.fpt.swp391.group6.DigitalTome.exception.AccountBannedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String email = request.getParameter("username");
        if (exception.getCause() instanceof AccountBannedException) {

            response.sendRedirect(request.getContextPath() + "/login?banned=true&email=" + URLEncoder.encode(email, StandardCharsets.UTF_8));
        } else {
            response.sendRedirect(request.getContextPath() + "/login?error");
        }
    }
}
