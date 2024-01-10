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

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public User signup(RegisterDTO registerDTO) {
        if (userRepository.existsUserByEmail(registerDTO.email())) throw new IllegalArgumentException("Email " + registerDTO.email() + " est déjà utilisé.");
        User user = registerDTO.toEntity(passwordEncoder);
        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequestDTO loginRequestDTO) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.email(),
                loginRequestDTO.password()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequestDTO.email())
                .build();
    }
}
