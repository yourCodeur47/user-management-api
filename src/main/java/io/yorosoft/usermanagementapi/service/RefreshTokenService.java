package io.yorosoft.usermanagementapi.service;

import io.yorosoft.usermanagementapi.model.RefreshToken;
import io.yorosoft.usermanagementapi.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * RefreshTokenService is a service class responsible for handling refresh tokens.
 * It uses RefreshTokenRepository for interacting with the RefreshToken database.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Generates a new refresh token.
     * It creates a new RefreshToken object with a random UUID as the token and the current time as the creation date,
     * and saves it to the database.
     * @return the generated RefreshToken
     */
    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreationDate(Instant.now());

        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * Validates the refresh token.
     * It checks if the token exists in the database and throws a RuntimeException if it does not.
     * @param token the token to validate
     */
    void validateRefreshToken(String token) {
        refreshTokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Invalid refresh Token"));
    }

    /**
     * Deletes the refresh token.
     * It removes the token from the database.
     * @param token the token to delete
     */
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
