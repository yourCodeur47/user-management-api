package io.yorosoft.usermanagementapi.service;

import io.yorosoft.usermanagementapi.config.TestConfigurer;
import io.yorosoft.usermanagementapi.model.RefreshToken;
import io.yorosoft.usermanagementapi.repository.RefreshTokenRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class RefreshTokenServiceTest extends TestConfigurer {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Test
    void sould_generate_refresh_token_when_generateRefreshToken(){

        String token = UUID.randomUUID().toString();
        Instant creationDate = Instant.now();

        RefreshToken savedRefreshToken = RefreshToken.builder().id(1L).token(token).creationDate(creationDate).build();

        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(savedRefreshToken);

        RefreshToken newRefreshToken = refreshTokenService.generateRefreshToken();

        assertThat(newRefreshToken).isNotNull();
        assertThat(newRefreshToken.getId()).isEqualTo(savedRefreshToken.getId());
        assertThat(newRefreshToken.getToken()).isEqualTo(token);

        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
    }

    @Test
    void sould_validate_refresh_token_when_validateRefreshToken(){
        String token = UUID.randomUUID().toString();
        Instant creationDate = Instant.now();
        RefreshToken savedRefreshToken = RefreshToken.builder().id(1L).token(token).creationDate(creationDate).build();
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(savedRefreshToken);
        RefreshToken newRefreshToken = refreshTokenService.generateRefreshToken();

        when(refreshTokenRepository.findByToken(any(String.class))).thenReturn(Optional.of(newRefreshToken));

        refreshTokenService.validateRefreshToken(token);

        verify(refreshTokenRepository, times(1)).findByToken(token);

    }

    @Test
    void sould_exception_when_validateRefreshToken(){
        String token = UUID.randomUUID().toString();
        Instant creationDate = Instant.now();
        RefreshToken savedRefreshToken = RefreshToken.builder().id(1L).token(token).creationDate(creationDate).build();
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(savedRefreshToken);
        RefreshToken newRefreshToken = refreshTokenService.generateRefreshToken();

        when(refreshTokenRepository.findByToken(any(String.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            refreshTokenService.validateRefreshToken(token);
        }).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Invalid refresh Token");

    }

    @Test
    void sould_delete_refreshtoken_when_delete_deleteRefreshToken(){
        String token = UUID.randomUUID().toString();

        refreshTokenService.deleteRefreshToken(token);

        verify(refreshTokenRepository, times(1)).deleteByToken(token);

    }
}
