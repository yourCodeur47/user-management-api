package io.yorosoft.usermanagementapi.config;

import io.yorosoft.usermanagementapi.dto.RegisterDTO;
import io.yorosoft.usermanagementapi.enums.Role;
import io.yorosoft.usermanagementapi.model.RefreshToken;
import io.yorosoft.usermanagementapi.model.Token;
import io.yorosoft.usermanagementapi.model.User;

import java.time.Instant;
import java.util.UUID;

public class TestConfigurer {

    protected RegisterDTO getRegisterDTO(String firstname, String lastname, String email, String password, Role role) {
        return new RegisterDTO(firstname, lastname, email, password, role);
    }

    protected User getUser(String firstname, String lastname, String email, String password, Role role) {
        return User.builder()
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .password(password)
                .role(role)
                .build();
    }

    protected Token getToken(User user) {
        String token = UUID.randomUUID().toString();
        return Token.builder()
                .token(token)
                .user(user).build();
    }

    protected RefreshToken getRefreshToken(String token, Instant creationDate) {
        return RefreshToken.builder().token(token).creationDate(creationDate).build();
    }


}
