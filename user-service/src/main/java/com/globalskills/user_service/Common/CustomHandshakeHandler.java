package com.globalskills.user_service.Common;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        String userId = null;

        try {
            if (request instanceof ServletServerHttpRequest servletRequest) {
                var httpRequest = servletRequest.getServletRequest();

                userId = httpRequest.getHeader("X-USER-ID");

                System.out.println("=== WebSocket Handshake ===");
                System.out.println("X-USER-ID: " + userId);
                System.out.println("==========================");

                if (userId == null || userId.isEmpty()) {
                    userId = "anonymous-" + UUID.randomUUID().toString().substring(0, 8);
                    System.out.println("⚠️ No X-USER-ID, using: " + userId);
                }
            } else {
                userId = "anonymous-" + UUID.randomUUID().toString().substring(0, 8);
            }

        } catch (Exception e) {
            System.err.println("❌ Error in determineUser: " + e.getMessage());
            e.printStackTrace();
            userId = "anonymous-error-" + UUID.randomUUID().toString().substring(0, 8);
        }

        final String finalUserId = userId;
        return () -> finalUserId;
    }
}
