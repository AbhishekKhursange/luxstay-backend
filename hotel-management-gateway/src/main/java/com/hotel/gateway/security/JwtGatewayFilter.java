package com.hotel.gateway.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.*;
import org.springframework.core.Ordered;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.hotel.gateway.service.TokenService;

import reactor.core.publisher.Mono;

@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(JwtGatewayFilter.class);

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenService tokenService;

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        log.info("Incoming request: {}", path);

        // 🚫 BLOCK ALL INTERNAL APIs COMPLETELY
        if (path.contains("/internal")) {
            String internalHeader = exchange.getRequest()
                    .getHeaders()
                    .getFirst("X-Internal-Call");

            if (!"true".equals(internalHeader)) {
                return forbidden(exchange);
            }
        }

        // Public APIs
        if (!validator.isSecured.test(exchange.getRequest())) {
            return chain.filter(exchange);
        }

        // JWT VALIDATION
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange);
        }

        String token = authHeader.substring(7);

        return tokenService.isBlacklisted(token).flatMap(isBlacklisted -> {

            if (Boolean.TRUE.equals(isBlacklisted)) {
                return unauthorized(exchange);
            }

            try {
                String username = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token);

                // ROLE CHECKS
               // ADMIN APIs
                if (path.startsWith("/admin") && !"ADMIN".equals(role)) {
                    return forbidden(exchange);
                }
                
               // USER APIs
                if (path.startsWith("/user") &&
                        !("USER".equals(role) || "ADMIN".equals(role))) {
                    return forbidden(exchange);
                }

               // HOTEL APIs
                if (path.startsWith("/hotel")) {
                    HttpMethod method = exchange.getRequest().getMethod();
                 // ONLY ADMIN CAN CREATE/DELETE HOTELS
                    if ((method == HttpMethod.POST || method == HttpMethod.DELETE)
                            && !"ADMIN".equals(role)) {
                        return forbidden(exchange);
                    }
                }
                
               // ROOM APIs
                if (path.startsWith("/room")) {

                    HttpMethod method =
                            exchange.getRequest().getMethod();

                    // ONLY ADMIN CAN CREATE/UPDATE/DELETE ROOMS
                    if ((method == HttpMethod.POST
                            || method == HttpMethod.PUT
                            || method == HttpMethod.DELETE)
                            && !"ADMIN".equals(role)) {

                        return forbidden(exchange);
                    }
                }
                
                // UPLOAD APIs — ADMIN only
                if (path.startsWith("/upload") && !"ADMIN".equals(role)) {
                    return forbidden(exchange);
                }

               // BOOKING APIs
                if (path.startsWith("/booking") &&
                        !("USER".equals(role) || "ADMIN".equals(role))) {
                    return forbidden(exchange);
                }

               // PAYMENT APIs
                if (path.startsWith("/payment") &&
                        !("USER".equals(role) || "ADMIN".equals(role))) {
                    return forbidden(exchange);
                }

              // ADD USER DETAILS TO REQUEST
                ServerHttpRequest request = exchange.getRequest()
                        .mutate()
                        .header("X-User-Name", username)
                        .header("X-User-Role", role)
                        .build();

                return chain.filter(exchange.mutate().request(request).build());

            } catch (Exception e) {
            	log.error("JWT validation failed: {}",
                        e.getMessage());
                return unauthorized(exchange);
            }
        });
    }

    // 401 UNAUTHORIZED
    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    // 403 FORBIDDEN
    private Mono<Void> forbidden(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }
}