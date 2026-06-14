package com.hotel.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class TokenService {

    @Autowired
    private ReactiveStringRedisTemplate redisTemplate;

    public Mono<Boolean> isBlacklisted(String token) {
        return redisTemplate.hasKey("BLACKLIST_" + token);
    }
}