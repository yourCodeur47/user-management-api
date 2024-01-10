package io.yorosoft.usermanagementapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.yorosoft.usermanagementapi.config.TestConfigurer;
import io.yorosoft.usermanagementapi.dto.LoginRequestDTO;
import io.yorosoft.usermanagementapi.dto.LoginResponse;
import io.yorosoft.usermanagementapi.dto.RegisterDTO;
import io.yorosoft.usermanagementapi.enums.Role;
import io.yorosoft.usermanagementapi.model.User;
import io.yorosoft.usermanagementapi.service.AuthService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthControllerTest extends TestConfigurer {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    ObjectMapper objectMapper;

    private String baseUrl = "http://localhost:8090/api/auth";

    @Test
    void should_add_user_success_when_signup() throws Exception {

        var registerDTO = getRegisterDTO("Ange Carmel", "YORO","yoro@gmail.com", "codeur47", Role.USER);

        var savedUser = User.builder()
                .id(1L)
                .firstname("Ange Carmel")
                .lastname("YORO")
                .email("yoro@gmail.com")
                .role(Role.USER)
                .build();

        when(this.authService.signup(any(RegisterDTO.class))).thenReturn(savedUser);

        this.mockMvc.perform(post(baseUrl.concat("/signup")).contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(registerDTO)).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("User created width success"))
                .andExpect(jsonPath("$.data.firstname").value(savedUser.getFirstname()))
                .andExpect(jsonPath("$.data.lastname").value(savedUser.getLastname()))
                .andExpect(jsonPath("$.data.email").value(savedUser.getEmail()))
                .andExpect(jsonPath("$.data.role").value(savedUser.getRole().toString()));

    }

    @ParameterizedTest
    @MethodSource("provideRegisterDtoForTesting")
    void should_throws_exception_when_signup_with_invalid_data(RegisterDTO registerDTO, String expectedMessage, String jsonPathData) throws Exception {

        var registerDTOJson = this.objectMapper.writeValueAsString(registerDTO);

        when(this.authService.signup(any(RegisterDTO.class))).thenThrow(new IllegalArgumentException("Provided arguments are invalid, see data for details."));

        this.mockMvc.perform(post(baseUrl.concat("/signup"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerDTOJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(jsonPath("$.data['" + jsonPathData + "']").value(expectedMessage));

    }

    @Test
    void should_throws_IllegalArgumentException_when_signup_with_existing_email() throws Exception {

        RegisterDTO registerDTOWidthSameEmail = getRegisterDTO("John", "DOE","yoro@gmail.com", "john47", Role.ADMIN);

        when(this.authService.signup(any(RegisterDTO.class))).thenThrow(new IllegalArgumentException("Email " + registerDTOWidthSameEmail.email() + " est déjà utilisé."));

        this.mockMvc.perform(post(baseUrl.concat("/signup"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(registerDTOWidthSameEmail))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Email " + registerDTOWidthSameEmail.email() + " est déjà utilisé."));

        verify(this.authService, times(1)).signup(any(RegisterDTO.class));
    }

    @Test
    void should_login_when_login_width_existing_user() throws Exception {

        String mail = "test1@gmail.com";
        String password = "codeur47";

        LoginRequestDTO loginRequestDTO = getLoginRequestDTO(mail, password);

        var loginResponse = LoginResponse.builder()
                        .token("token")
                        .refreshToken("refreshtoken")
                        .expiresAt(Instant.now())
                        .username(mail)
                        .build();

        when(this.authService.login(any())).thenReturn(loginResponse);

        this.mockMvc.perform(post(baseUrl.concat("/login")).contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(loginRequestDTO)).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("User login width success"))
                .andExpect(jsonPath("$.data.token").value(loginResponse.token()))
                .andExpect(jsonPath("$.data.refreshToken").value(loginResponse.refreshToken()))
                .andExpect(jsonPath("$.data.username").value(loginResponse.username()));
    }

    @ParameterizedTest
    @MethodSource("provideLoginRequestDTOForTesting")
    void should_throws_exception_when_login_with_invalid_data(LoginRequestDTO loginRequestDTO, String expectedMessage, String jsonPathData) throws Exception {

        when(this.authService.login(any(LoginRequestDTO.class))).thenThrow(new IllegalArgumentException("Provided arguments are invalid, see data for details."));

        this.mockMvc.perform(post(baseUrl.concat("/signup"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(loginRequestDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(jsonPath("$.data['" + jsonPathData + "']").value(expectedMessage));
    }
}
