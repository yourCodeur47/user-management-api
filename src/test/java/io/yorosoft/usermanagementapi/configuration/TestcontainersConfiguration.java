package io.yorosoft.usermanagementapi.configuration;

import io.yorosoft.usermanagementapi.enums.Role;
import io.yorosoft.usermanagementapi.model.Token;
import io.yorosoft.usermanagementapi.model.User;

import java.security.SecureRandom;

public class TestcontainersConfiguration {


    protected User getUser(String firstName, String lastName, String email, Role role) {
        return User.builder()
                .firstname(firstName)
                .lastname(lastName)
                .email(email)
                .role(role)
                .build();
    }

    protected Token getToken(boolean revoked, boolean expired, User user) {
        return Token
                .builder()
                .token(generateAlphaNumericString())
                .user(user)
                .build();
    }

    public String generateAlphaNumericString() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(200);
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        for (int i = 0; i < 200; i++) {
            int randomIndex = secureRandom.nextInt(chars.length());
            stringBuilder.append(chars.charAt(randomIndex));
        }

        return stringBuilder.toString();
    }
}
