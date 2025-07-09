package com.example.hotcinemas_be.services;


public interface BlackListService {
    Boolean isTokenBlacklisted(String token);
    void saveTokenToBlacklist(String token, String value);
}
