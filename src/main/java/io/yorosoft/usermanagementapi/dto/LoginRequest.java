package io.yorosoft.usermanagementapi.dto;

public record LoginRequest(
        String email,
        String password
) {
}
