package com.example.hotcinemas_be.services;

import org.springframework.stereotype.Service;

public interface BlackListService {
    Boolean isTokenBlacklisted(String token);
    void saveTokenToBlacklist(String token, String value);
}
