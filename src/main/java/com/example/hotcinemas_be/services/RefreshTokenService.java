package com.example.hotcinemas_be.services;

import com.example.hotcinemas_be.dtos.requests.RefreshTokenRequest;
import com.example.hotcinemas_be.dtos.responses.RefreshTokenResponse;

public interface RefreshTokenService {
    RefreshTokenResponse createRefreshToken(RefreshTokenRequest refreshTokenRequest);
}
