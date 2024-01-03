package io.yorosoft.usermanagementapi.dto;

import io.yorosoft.usermanagementapi.enums.Role;
import io.yorosoft.usermanagementapi.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public record RegisterDTO(
        String firstname,
        String lastname,
        String email,
        String password,
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
