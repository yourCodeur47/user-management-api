package io.yorosoft.usermanagementapi.service;

import io.yorosoft.usermanagementapi.model.Token;
import io.yorosoft.usermanagementapi.model.User;
import io.yorosoft.usermanagementapi.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenService {

    private final TokenRepository tokenRepository;

    public String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        Token verificationToken = Token.builder()
                .token(token)
                .user(user).build();

        tokenRepository.save(verificationToken);
        return token;
    }
}
