package io.yorosoft.usermanagementapi.repository;

import io.yorosoft.usermanagementapi.configuration.TestcontainersConfiguration;
import io.yorosoft.usermanagementapi.enums.Role;
import io.yorosoft.usermanagementapi.model.Token;
import io.yorosoft.usermanagementapi.model.User;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TokenRepositoryTest extends TestcontainersConfiguration {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeAll
    public static void startDatabase() {
        postgreSQLContainer.start();
        if (postgreSQLContainer.isRunning()) {
            Flyway flyway = Flyway.configure()
                    .dataSource(postgreSQLContainer.getJdbcUrl(),
                            postgreSQLContainer.getUsername(),
                            postgreSQLContainer.getPassword())
                    .locations("classpath:db/migration")
                    .cleanDisabled(false)
                    .load();
            flyway.clean();
            flyway.migrate();
        }

    }

    @BeforeEach
    public void initDatabase() {
        if (postgreSQLContainer.isRunning()) {
            Flyway flyway = Flyway.configure()
                    .dataSource(postgreSQLContainer.getJdbcUrl(),
                            postgreSQLContainer.getUsername(),
                            postgreSQLContainer.getPassword())
                    .locations("classpath:db/migration")
                    .cleanDisabled(false)
                    .load();
            flyway.clean();
            flyway.migrate();
        }

    }

    @AfterAll
    public static void stopDatabase() {
        postgreSQLContainer.stop();
    }

    @Test
    void when_findAllValidTokenByUser_width_correct_user_id_should_return_token_list() {
        /*User user = getUser("Theo", "Hernandez", "theo@gmail.com", Role.USER);
        Token token = getToken(false, false, user);

        User savedUser = userRepository.save(user);
        tokenRepository.save(token);

        List<Token> tokens = tokenRepository.findAllValidTokenByUser(savedUser.getId());

        assertThat(tokens)
                .isNotEmpty()
                .hasSize(1);*/

    }

    @Test
    void when_findAllValidTokenByUser_width_wrong_user_id_should_return_empty_list() {
        /*User user = getUser("Theo", "Hernandez", "theo@gmail.com", Role.USER);
        Token token = getToken(false, false, user);

        User savedUser = userRepository.save(user);
        tokenRepository.save(token);

        List<Token> tokens = tokenRepository.findAllValidTokenByUser(15);

        assertThat(tokens)
                .isEmpty();*/

    }

}
