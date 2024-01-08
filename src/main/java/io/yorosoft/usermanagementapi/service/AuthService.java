package io.yorosoft.usermanagementapi.service;

import io.yorosoft.usermanagementapi.dto.RegisterDTO;
import io.yorosoft.usermanagementapi.model.User;
import io.yorosoft.usermanagementapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public User signup(RegisterDTO registerDTO) {
        if (userRepository.existsUserByEmail(registerDTO.email())) throw new IllegalArgumentException("Email " + registerDTO.email() + " est déjà utilisé.");
        User user = registerDTO.toEntity(passwordEncoder);
        return userRepository.save(user);
    }
}
