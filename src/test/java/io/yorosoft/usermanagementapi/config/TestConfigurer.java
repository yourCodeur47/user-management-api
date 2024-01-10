package io.yorosoft.usermanagementapi.config;

import io.yorosoft.usermanagementapi.dto.LoginRequestDTO;
import io.yorosoft.usermanagementapi.dto.RegisterDTO;
import io.yorosoft.usermanagementapi.enums.Role;
import io.yorosoft.usermanagementapi.model.RefreshToken;
import io.yorosoft.usermanagementapi.model.Token;
import io.yorosoft.usermanagementapi.model.User;
import org.junit.jupiter.params.provider.Arguments;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;

public class TestConfigurer {

    protected static RegisterDTO getRegisterDTO(String firstname, String lastname, String email, String password, Role role) {
        return new RegisterDTO(firstname, lastname, email, password, role);
    }

    protected static LoginRequestDTO getLoginRequestDTO(String email, String password) {
        return new LoginRequestDTO(email, password);
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

    protected static Stream<Arguments> provideRegisterDtoForTesting() {
        return Stream.of(
                Arguments.of(getRegisterDTO("", "YORO","yoro@gmail.com", "codeur47", Role.USER),
                        "FirstName must not be blank", "firstname"),
                Arguments.of(getRegisterDTO("Ange Carmel", "","yoro@gmail.com", "codeur47", Role.USER),
                        "LastName must not be blank", "lastname"),
                Arguments.of(getRegisterDTO("Ange Carmel", "YORO","", "codeur47", Role.USER),
                        "Email must not be blank", "email"),
                Arguments.of(getRegisterDTO("Ange Carmel", "YORO","yoro@gmail.com", "", Role.USER),
                        "Password must not be blank", "password"),
                Arguments.of(getRegisterDTO("Ange Carmel", "YORO","yoro@gmail.com", "codeur47", null),
                        "Role must not be null", "role")
        );
    }

    protected static Stream<Arguments> provideLoginRequestDTOForTesting() {
        return Stream.of(
                Arguments.of(getLoginRequestDTO("", "codeur47"),
                        "Email must not be blank", "email"),
                Arguments.of(getLoginRequestDTO("test1@gmail.com", ""),
                        "Password must not be blank", "password")
        );
    }


}
