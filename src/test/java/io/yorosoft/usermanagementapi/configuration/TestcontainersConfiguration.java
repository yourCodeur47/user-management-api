package io.yorosoft.usermanagementapi.configuration;

import io.yorosoft.usermanagementapi.enums.Role;
import io.yorosoft.usermanagementapi.enums.TokenType;
import io.yorosoft.usermanagementapi.model.Token;
import io.yorosoft.usermanagementapi.model.User;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.security.SecureRandom;

@SpringBootTest
@Testcontainers
public class TestcontainersConfiguration {
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("user-management-api-test")
            .withUsername("postgres")
            .withPassword("codeur47");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

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
                .tokenType(TokenType.BEARER)
                .revoked(revoked)
                .expired(expired)
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
