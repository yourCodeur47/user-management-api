package io.yorosoft.usermanagementapi.repository;

import io.yorosoft.usermanagementapi.configuration.TestcontainersConfiguration;
import io.yorosoft.usermanagementapi.enums.Role;
import io.yorosoft.usermanagementapi.model.Token;
import io.yorosoft.usermanagementapi.model.User;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TokenRepositoryTest extends TestcontainersConfiguration {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public static void runMigrations(@Autowired DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();
    }

    @Test
    void when_findAllValidTokenByUser_width_correct_user_id_should_return_token_list() {
        User user = getUser("Theo", "Hernandez", "theo@gmail.com", Role.USER);
        Token token = getToken(false, false, user);

        User savedUser = userRepository.save(user);
        tokenRepository.save(token);

        List<Token> tokens = tokenRepository.findAllValidTokenByUser(savedUser.getId());

        assertThat(tokens)
                .isNotEmpty()
                .hasSize(1);

    }

    @Test
    void when_findAllValidTokenByUser_width_wrong_user_id_should_return_empty_list() {
        User user = getUser("Theo", "Hernandez", "theo@gmail.com", Role.USER);
        Token token = getToken(false, false, user);

        User savedUser = userRepository.save(user);
        tokenRepository.save(token);

        List<Token> tokens = tokenRepository.findAllValidTokenByUser(15);

        assertThat(tokens)
                .isEmpty();

    }

}
