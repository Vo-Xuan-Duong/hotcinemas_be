package com.example.hotcinemas_be.mappers;

import com.example.hotcinemas_be.dtos.responses.RefreshTokenResponse;
import com.example.hotcinemas_be.models.RefreshToken;
import lombok.AccessLevel;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenMapper {
    public RefreshTokenResponse mapToResponse(RefreshToken refreshToken) {
        return RefreshTokenResponse.builder()
                .tokenId(refreshToken.getTokenId())
                .token(refreshToken.getToken())
                .createdAt(refreshToken.getCreatedAt())
                .build();
    }
}
