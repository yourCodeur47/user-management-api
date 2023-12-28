package io.yorosoft.usermanagementapi.configuration;

import io.yorosoft.usermanagementapi.enums.Role;
import io.yorosoft.usermanagementapi.enums.TokenType;
import io.yorosoft.usermanagementapi.model.Token;
import io.yorosoft.usermanagementapi.model.User;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.security.SecureRandom;

@SpringBootTest
@Testcontainers
public class TestcontainersConfiguration {


    // Assuming setupDataSource is converted to
    private static DataSource setupDataSource(PostgreSQLContainer<?> postgreSQLContainer) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(postgreSQLContainer.getJdbcUrl());
        dataSource.setUser(postgreSQLContainer.getUsername());
        dataSource.setPassword(postgreSQLContainer.getPassword());
        return dataSource;
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
