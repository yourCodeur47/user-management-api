package io.yorosoft.usermanagementapi.dto;

public record TokenDTO(
        String token
) {
    public TokenDTO(TokenDTO token) {
        this(
                token.token()
        );
    }
}
