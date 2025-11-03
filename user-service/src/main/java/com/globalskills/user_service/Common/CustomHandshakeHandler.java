package com.globalskills.user_service.Common;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servletRequest) {
            var httpRequest = servletRequest.getServletRequest();
            String userId = httpRequest.getHeader("X-User-Id");

            if (userId == null || userId.isEmpty()) {
                userId = "anonymous-" + System.currentTimeMillis();
            }
            String finalUserId = userId;
            return () -> finalUserId;
        }
        return () -> "anonymous";
    }
}
