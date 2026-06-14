package com.hotel.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hotel.auth.dto.AuthRequest;
import com.hotel.auth.dto.AuthResponse;
import com.hotel.auth.dto.RegisterRequest;
import com.hotel.auth.dto.UserDto;
import com.hotel.auth.feign.UserClient;
import com.hotel.auth.security.JwtUtil;

@Service
public class AuthService {
	
	private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserClient userClient;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private TokenService tokenService;

    public String register(RegisterRequest request) {

    	log.info("Register request received for username: {}", request.getUsername());
        request.setPassword(encoder.encode(request.getPassword()));
        userClient.register(request);
        log.info("User registered successfully: {}", request.getUsername());
        
        return "User Registered Successfully";
    }


    public AuthResponse login(AuthRequest request) {

    	log.info("Login attempt for username: {}", request.getUsername());
    	
    	UserDto user = userClient.getUser(request.getUsername());

        if (user != null &&
            encoder.matches(request.getPassword(), user.getPassword())) {

        	log.info("Login successful for username: {}", request.getUsername());
        	
            String accessToken = jwtUtil.generateToken(
                    user.getUsername(),
                    user.getRole()
            );
            
            log.debug("Generated JWT token for user: {}", request.getUsername());

            String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

            tokenService.storeRefreshToken(user.getUsername(), refreshToken);

            return new AuthResponse(accessToken, refreshToken);
        }

        log.error("Invalid login attempt for username: {}", request.getUsername());
        
        throw new RuntimeException("Invalid Credentials");
    }
    
    public AuthResponse refresh(String refreshToken) {
    	
    	if (!jwtUtil.isTokenValid(refreshToken) || !jwtUtil.isRefreshToken(refreshToken)) {
    	    throw new RuntimeException("Invalid Refresh Token");
    	}

        String username = jwtUtil.extractUsername(refreshToken);

        String stored = tokenService.getRefreshToken(username);

        if (stored != null && stored.equals(refreshToken)) {

        	UserDto user = userClient.getUser(username);

            String newAccessToken = jwtUtil.generateToken(
                    user.getUsername(),
                    user.getRole()
            );

            return new AuthResponse(newAccessToken, refreshToken);
        }

        throw new RuntimeException("Invalid Refresh Token");
    }
}
