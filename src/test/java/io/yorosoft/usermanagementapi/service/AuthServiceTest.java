package io.yorosoft.usermanagementapi.service;

import io.yorosoft.usermanagementapi.config.TestConfigurer;
import io.yorosoft.usermanagementapi.dto.LoginRequestDTO;
import io.yorosoft.usermanagementapi.dto.LoginResponse;
import io.yorosoft.usermanagementapi.dto.RegisterDTO;
import io.yorosoft.usermanagementapi.enums.Role;
import io.yorosoft.usermanagementapi.model.RefreshToken;
import io.yorosoft.usermanagementapi.model.User;
import io.yorosoft.usermanagementapi.repository.UserRepository;
import io.yorosoft.usermanagementapi.security.JwtProvider;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthServiceTest extends TestConfigurer {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthService authService;

    @Test
    void should_signup_when_signup_width_valid_register_dto() {

        RegisterDTO registerDTO = getRegisterDTO("Ange Carmel", "YORO","yoro@gmail.com", "codeur47", Role.USER);

        User userToRegister = registerDTO.toEntity(passwordEncoder);

        when(userRepository.save(any(User.class))).thenReturn(userToRegister);

        User savedUser = authService.signup(registerDTO);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(userToRegister.getEmail());

        verify(userRepository, times(1)).existsUserByEmail(any(String.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void should_throws_IllegalArgumentException_when_signup_width_existing_email() {

        RegisterDTO registerDTOWidthSameEmail = getRegisterDTO("John", "DOE","yoro@gmail.com", "john47", Role.ADMIN);
        User userToRegisterWidthSameEmail = registerDTOWidthSameEmail.toEntity(passwordEncoder);
        when(userRepository.existsUserByEmail(any(String.class))).thenReturn(true);

        assertThatThrownBy(() -> {
            authService.signup(registerDTOWidthSameEmail);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email " + userToRegisterWidthSameEmail.getEmail() + " est déjà utilisé.");

        verify(userRepository, times(1)).existsUserByEmail(any(String.class));
        verify(userRepository, never()).save(any(User.class));

    }

    @Test
    void should_login_when_login_width_existing_user() {

        String email = "test@email.com";
        String password = "password";
        String token = "token";

        Authentication authentication = mock(Authentication.class);

        RefreshToken refreshToken = new RefreshToken(1L,"refreshToken", Instant.now());

        LoginRequestDTO loginRequestDTO = getLoginRequestDTO(email, password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtProvider.generateToken(any())).thenReturn(token);
        when(refreshTokenService.generateRefreshToken()).thenReturn(refreshToken);
        when(jwtProvider.getJwtExpirationInMillis()).thenReturn(900000L);

        LoginResponse loginResponse = authService.login(loginRequestDTO);

        assertThat(loginResponse.username()).isEqualTo("test@email.com");
        assertThat(loginResponse.token()).isEqualTo("token");
        assertThat(loginResponse.refreshToken()).isEqualTo("refreshToken");
    }


}
