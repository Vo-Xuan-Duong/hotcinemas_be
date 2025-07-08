package com.example.hotcinemas_be.services.ServiceImpls;

import com.example.hotcinemas_be.dtos.requests.RefreshTokenRequest;
import com.example.hotcinemas_be.dtos.responses.RefreshTokenResponse;
import com.example.hotcinemas_be.exceptions.ErrorCode;
import com.example.hotcinemas_be.exceptions.ErrorException;
import com.example.hotcinemas_be.mappers.RefreshTokenMapper;
import com.example.hotcinemas_be.models.RefreshToken;
import com.example.hotcinemas_be.repositorys.RefreshTokenRepository;
import com.example.hotcinemas_be.repositorys.UserRepository;
import com.example.hotcinemas_be.services.RefreshTokenService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenMapper refreshTokenMapper;
    private final UserRepository userRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository,
                                    RefreshTokenMapper refreshTokenMapper,
                                   UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenMapper = refreshTokenMapper;
        this.userRepository = userRepository;
    }

    public RefreshTokenResponse createRefreshToken(RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = RefreshToken.builder()
                .tokenId(refreshTokenRequest.getTokenId())
                .token(refreshTokenRequest.getToken())
                .user(userRepository.findByUsername(refreshTokenRequest.getUsername()).orElseThrow(() -> new ErrorException( "User not found", ErrorCode.ERROR_MODEL_NOT_FOUND)))
                .build();
        return refreshTokenMapper.mapToResponse(refreshTokenRepository.save(refreshToken));
    }

    public List<RefreshTokenResponse> getAllRefreshTokens(Pageable pageable) {
        List<RefreshToken> refreshTokens = refreshTokenRepository.findAll(pageable).getContent();
        if (refreshTokens.isEmpty()) {
            throw new ErrorException("No refresh tokens found", ErrorCode.ERROR_MODEL_NOT_FOUND);
        }
        return refreshTokens.stream()
                .map(refreshTokenMapper::mapToResponse)
                .toList();
    }

    public void deleteRefreshToken(String tokenId) {
        RefreshToken refreshToken = refreshTokenRepository.findById(tokenId)
                .orElseThrow(() -> new ErrorException("Refresh token not found", ErrorCode.ERROR_MODEL_NOT_FOUND));
        refreshTokenRepository.delete(refreshToken);
    }

    public RefreshTokenResponse getRefreshTokenById(String tokenId) {
        RefreshToken refreshToken = refreshTokenRepository.findById(tokenId)
                .orElseThrow(() -> new ErrorException("Refresh token not found", ErrorCode.ERROR_MODEL_NOT_FOUND));
        return refreshTokenMapper.mapToResponse(refreshToken);
    }

    public RefreshTokenResponse updateRefreshToken(String tokenId, RefreshTokenRequest refreshTokenRequest) {
        RefreshToken existingRefreshToken = refreshTokenRepository.findById(tokenId)
                .orElseThrow(() -> new ErrorException("Refresh token not found", ErrorCode.ERROR_MODEL_NOT_FOUND));

        existingRefreshToken.setToken(refreshTokenRequest.getToken());
        existingRefreshToken.setUser(userRepository.findByUsername(refreshTokenRequest.getUsername())
                .orElseThrow(() -> new ErrorException("User not found", ErrorCode.ERROR_MODEL_NOT_FOUND)));

        return refreshTokenMapper.mapToResponse(refreshTokenRepository.save(existingRefreshToken));
    }

    public boolean existsByTokenId(String tokenId) {
        return refreshTokenRepository.existsById(tokenId);
    }
}
