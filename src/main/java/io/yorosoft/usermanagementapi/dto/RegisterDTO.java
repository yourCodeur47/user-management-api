package io.yorosoft.usermanagementapi.dto;

import io.yorosoft.usermanagementapi.enums.Role;
import io.yorosoft.usermanagementapi.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record RegisterDTO(
        @NotBlank
        String firstname,

        @NotBlank
        String lastname,

        @NotBlank
        String email,

        @NotBlank
        String password,

        @NotNull
        Role role
) {

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .role(role)
                .password(passwordEncoder.encode(password))
                .build();
    }
}
