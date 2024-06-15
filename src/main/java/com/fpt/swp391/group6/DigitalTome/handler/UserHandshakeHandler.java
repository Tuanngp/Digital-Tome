package com.fpt.swp391.group6.DigitalTome.handler;

import com.sun.security.auth.UserPrincipal;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public class UserHandshakeHandler extends DefaultHandshakeHandler {
    private final Logger LOG = LoggerFactory.getLogger(UserHandshakeHandler.class);

    @Override
    protected Principal determineUser(@NotNull ServerHttpRequest request,
                                      @NotNull WebSocketHandler wsHandler,
                                      @NotNull Map<String, Object> attributes) {
        final String randomId = UUID.randomUUID().toString();
        LOG.info("User with ID '{}' opened the page", randomId);

        return new UserPrincipal(randomId);
    }
}