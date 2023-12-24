package io.yorosoft.usermanagementapi.repository;

import io.yorosoft.usermanagementapi.configuration.TestcontainersConfiguration;
import io.yorosoft.usermanagementapi.enums.Role;
import io.yorosoft.usermanagementapi.model.User;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class UserRepositoryTest extends TestcontainersConfiguration {

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
    public void when_findByEmail_width_correct_email_should_return_correct_user() {
        String email = "theo@gmail.com";
        User user = getUser("Theo", "Hernandez", email, Role.USER);
        userRepository.save(user);
        Optional<User> savedUser = userRepository.findByEmail(email);
        assertThat(savedUser.isPresent()).isTrue();
        assertThat(savedUser.get().getEmail()).isEqualTo(email);
    }

    @Test
    public void when_findByEmail_width_wrong_email_should_return_empty() {
        String email = "toto@gmail.com";
        User user = getUser("Theo", "Hernandez", "theo@gmail.com", Role.USER);
        userRepository.save(user);
        Optional<User> savedUser = userRepository.findByEmail(email);
        assertThat(savedUser.isPresent()).isFalse();
    }

    private User getUser(String firstName, String lastName, String email, Role role) {
        return User.builder()
                .firstname(firstName)
                .lastname(lastName)
                .email(email)
                .role(role)
                .build();
    }
}
