package io.yorosoft.usermanagementapi.dto;

public record AuthenticationPostDTO(
        String email,
        String password
) {
}
