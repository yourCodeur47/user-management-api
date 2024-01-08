package io.yorosoft.usermanagementapi.dto;

import io.yorosoft.usermanagementapi.enums.Role;
import io.yorosoft.usermanagementapi.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;

public record RegisterDTO(
        @NotBlank(message = "FirstName must not be blank")
        String firstname,
        @NotBlank(message = "LastName must not be blank")
        String lastname,
        @NotBlank(message = "Email must not be blank")
        String email,
        @NotBlank(message = "Password must not be blank")
        String password,
        @NotNull(message = "Role must not be null")
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
