package io.yorosoft.usermanagementapi.service;

import io.yorosoft.usermanagementapi.config.TestConfigurer;
import io.yorosoft.usermanagementapi.dto.RegisterDTO;
import io.yorosoft.usermanagementapi.enums.Role;
import io.yorosoft.usermanagementapi.model.User;
import io.yorosoft.usermanagementapi.repository.UserRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AuthServiceTest extends TestConfigurer {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void when_signup_width_valide_register_should_create_user() {

        RegisterDTO registerDTO = getRegisterDTO("Ange Carmel", "YORO","yoro@gmail.com", "codeur47", Role.USER);

        User userToRegister = registerDTO.toEntity(passwordEncoder);

        when(userRepository.save(any(User.class))).thenReturn(userToRegister);

        User savedUser = authService.signup(registerDTO);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(userToRegister.getEmail());


    }


}
