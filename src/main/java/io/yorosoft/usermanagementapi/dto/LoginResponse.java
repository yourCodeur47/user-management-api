package io.yorosoft.usermanagementapi.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record LoginResponse(
        String token,
        String refreshToken,
        Instant expiresAt,
        String username
) {
}
