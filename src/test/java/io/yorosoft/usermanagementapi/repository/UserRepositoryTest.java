package io.yorosoft.usermanagementapi.repository;

import io.yorosoft.usermanagementapi.configuration.TestcontainersConfiguration;
import io.yorosoft.usermanagementapi.enums.Role;
import io.yorosoft.usermanagementapi.model.User;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
@Testcontainers
public class UserRepositoryTest extends TestcontainersConfiguration {

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
    void when_findByEmail_width_correct_email_should_return_correct_user() {
        String email = "theo@gmail.com";
        User user = getUser("Theo", "Hernandez", email, Role.USER);
        userRepository.save(user);
        Optional<User> savedUser = userRepository.findByEmail(email);
        assertThat(savedUser.isPresent()).isTrue();
        assertThat(savedUser.get().getEmail()).isEqualTo(email);
    }

    @Test
    void when_findByEmail_width_wrong_email_should_return_empty() {
        String email = "toto@gmail.com";
        User user = getUser("Theo", "Hernandez", "theo@gmail.com", Role.USER);
        userRepository.save(user);
        Optional<User> savedUser = userRepository.findByEmail(email);
        assertThat(savedUser.isPresent()).isFalse();
    }
}
