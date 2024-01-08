package io.yorosoft.usermanagementapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.yorosoft.usermanagementapi.config.TestConfigurer;
import io.yorosoft.usermanagementapi.dto.RegisterDTO;
import io.yorosoft.usermanagementapi.dto.UserInfoDTO;
import io.yorosoft.usermanagementapi.enums.Role;
import io.yorosoft.usermanagementapi.model.User;
import io.yorosoft.usermanagementapi.service.AuthService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AuthControllerTest extends TestConfigurer {

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

        var registerDTOJson = this.objectMapper.writeValueAsString(registerDTO);

        var savedUser = User.builder()
                .id(1L)
                .firstname("Ange Carmel")
                .lastname("YORO")
                .email("yoro@gmail.com")
                .role(Role.USER)
                .build();

        when(this.authService.signup(any(RegisterDTO.class))).thenReturn(savedUser);

        this.mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(registerDTOJson).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("User created width success"))
                .andExpect(jsonPath("$.data.firstname").value(savedUser.getFirstname()))
                .andExpect(jsonPath("$.data.lastname").value(savedUser.getLastname()))
                .andExpect(jsonPath("$.data.email").value(savedUser.getEmail()))
                .andExpect(jsonPath("$.data.role").value(savedUser.getRole().toString()));

    }
}
