package com.hotel.gateway.security;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Component
public class RouteValidator {

    // Public APIs
    public static final List<String> openApiEndpoints = List.of(
            "/auth/register",
            "/auth/login",
            "/auth/refresh"
    );
    
    public static final List<String> publicGetPaths = List.of(
            "/hotel",
            "/room"
        );

    public Predicate<ServerHttpRequest> isSecured =
            request -> {
                String path = request.getURI().getPath();
                HttpMethod method = request.getMethod();

                // Auth endpoints — always public
                boolean isAuthEndpoint = openApiEndpoints.stream()
                    .anyMatch(uri -> path.contains(uri));

                // Hotel/Room GET — public for browsing
                boolean isPublicGet = method == HttpMethod.GET &&
                    publicGetPaths.stream()
                        .anyMatch(uri -> path.startsWith(uri));

                return !isAuthEndpoint && !isPublicGet;
            };
}