package com.hotel.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.hotel.auth.security.JwtUtil;

import java.util.concurrent.TimeUnit;

@Service
public class TokenService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @Autowired
    private JwtUtil jwtUtil;

    public void storeRefreshToken(String username, String refreshToken) {
        redisTemplate.opsForValue().set(
                "REFRESH_" + username,
                refreshToken,
                7,
                TimeUnit.DAYS
        );
    }

    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get("REFRESH_" + username);
    }

    public void blacklistToken(String token) {
        long expiry = jwtUtil.getExpiryDuration(token); // remaining ms

        redisTemplate.opsForValue().set(
                "BLACKLIST_" + token,
                "true",
                expiry,
                TimeUnit.MILLISECONDS
        );
    }

    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey("BLACKLIST_" + token);
    }
}
