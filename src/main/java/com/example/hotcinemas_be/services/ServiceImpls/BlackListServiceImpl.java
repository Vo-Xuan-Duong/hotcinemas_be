package com.example.hotcinemas_be.services.ServiceImpls;

import com.example.hotcinemas_be.services.BlackListService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class BlackListServiceImpl implements BlackListService {

    private final RedisTemplate<String , String> redisTemplate;

    public BlackListServiceImpl(RedisTemplate<String , String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }

    public void saveTokenToBlacklist(String token, String value) {
        redisTemplate.opsForValue().set(token, value, 60, java.util.concurrent.TimeUnit.MINUTES);
    }

}
