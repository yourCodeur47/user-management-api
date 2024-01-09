package io.yorosoft.usermanagementapi.service;


import io.yorosoft.usermanagementapi.config.TestConfigurer;
import io.yorosoft.usermanagementapi.enums.Role;
import io.yorosoft.usermanagementapi.model.Token;
import io.yorosoft.usermanagementapi.model.User;
import io.yorosoft.usermanagementapi.repository.TokenRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TokenServiceTest extends TestConfigurer {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenService tokenService;

    @Test
    void should_generate_token_width_user() {

        User user = getUser("Ange Carmel", "YORO", "ange@gmail.com", "codeur47", Role.ADMIN);
        Token token = getToken(user);

        when(tokenRepository.save(any(Token.class))).thenReturn(token);

        String savedToken = tokenService.generateVerificationToken(user);

        assertThat(savedToken).isNotNull();
    }
}
