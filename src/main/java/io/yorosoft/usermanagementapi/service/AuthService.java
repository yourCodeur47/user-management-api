package io.yorosoft.usermanagementapi.service;

import io.yorosoft.usermanagementapi.dto.LoginRequestDTO;
import io.yorosoft.usermanagementapi.dto.LoginResponse;
import io.yorosoft.usermanagementapi.dto.RegisterDTO;
import io.yorosoft.usermanagementapi.model.User;
import io.yorosoft.usermanagementapi.repository.UserRepository;
import io.yorosoft.usermanagementapi.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * AuthService is a service class responsible for handling user authentication and registration.
 * It uses Spring Security's AuthenticationManager for authenticating users and JwtProvider for generating JWT tokens.
 */

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    /**
     * Handles user registration.
     * It validates the email and saves the user to the database.
     * @param registerDTO the data transfer object containing the user registration details
     * @return the registered User
     */
    public User signup(RegisterDTO registerDTO) {
        validateEmail(registerDTO.email());
        return userRepository.save(registerDTO.toEntity(passwordEncoder));
    }

    /**
     * Handles user login.
     * It authenticates the user and creates a login response containing the JWT token and refresh token.
     * @param loginRequestDTO the data transfer object containing the user login details
     * @return the LoginResponse containing the JWT token and refresh token
     */
    public LoginResponse login(LoginRequestDTO loginRequestDTO) {
        Authentication authenticate = authenticateUser(loginRequestDTO);
        return createLoginResponse(authenticate, loginRequestDTO.email());
    }

    /**
     * Validates the email.
     * It checks if the email already exists in the database and throws an IllegalArgumentException if it does.
     * @param email the email to validate
     */
    private void validateEmail(String email) {
        if (userRepository.existsUserByEmail(email)) {
            throw new IllegalArgumentException("Email " + email + " est déjà utilisé.");
        }
    }

    /**
     * Authenticates the user.
     * It uses the AuthenticationManager to authenticate the user and sets the authenticated user in the SecurityContext.
     * @param loginRequestDTO the data transfer object containing the user login details
     * @return the authenticated Authentication
     */
    private Authentication authenticateUser(LoginRequestDTO loginRequestDTO) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.email(), loginRequestDTO.password()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return authenticate;
    }

    /**
     * Creates the login response.
     * It generates a JWT token and creates a LoginResponse containing the token, refresh token, expiration time, and username.
     * @param authenticate the authenticated Authentication
     * @param username the username
     * @return the LoginResponse containing the JWT token and refresh token
     */
    private LoginResponse createLoginResponse(Authentication authenticate, String username) {
        String token = jwtProvider.generateToken(authenticate);
        return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(username)
                .build();
    }
}

