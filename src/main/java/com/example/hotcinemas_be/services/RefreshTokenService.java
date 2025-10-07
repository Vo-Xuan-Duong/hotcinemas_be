package com.example.hotcinemas_be.services;

import com.example.hotcinemas_be.dtos.auth.requests.RefreshTokenRequest;

public interface RefreshTokenService {
    void createRefreshToken(RefreshTokenRequest refreshTokenRequest);

    void deleteRefreshToken(String refreshToken);
}
