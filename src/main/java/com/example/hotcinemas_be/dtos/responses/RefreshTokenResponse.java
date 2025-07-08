package com.example.hotcinemas_be.dtos.responses;

import com.example.hotcinemas_be.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponse {
    private String tokenId;
    private String token;
    private LocalDateTime createdAt;
}
