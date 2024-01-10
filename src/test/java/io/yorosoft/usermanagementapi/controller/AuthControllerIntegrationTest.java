package io.yorosoft.usermanagementapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.yorosoft.usermanagementapi.config.TestConfigurer;
import io.yorosoft.usermanagementapi.dto.LoginRequestDTO;
import io.yorosoft.usermanagementapi.dto.RegisterDTO;
import io.yorosoft.usermanagementapi.enums.Role;
import io.yorosoft.usermanagementapi.service.AuthService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Testcontainers
@ExtendWith(SpringExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerIntegrationTest extends TestConfigurer {

    public static final String API_AUTH_URL = "/api/auth/";
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @BeforeAll
    static void startDatabase() {
        postgreSQLContainer.start();
    }

    @BeforeEach
    void initPort(){
        RestAssured.port = port;
    }

    @AfterAll
    static void stopDatabase() {
        postgreSQLContainer.stop();
    }

    @Test
    void should_add_user_success_when_signup() throws Exception {

        RegisterDTO registerDTO = getRegisterDTO("Ange Carmel", "YORO","yoro@gmail.com", "codeur47", Role.USER);

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(registerDTO))
            .when()
            .post(API_AUTH_URL.concat("signup"))
            .then()
            .statusCode(200)
            .body("flag", equalTo(true))
            .body("code", equalTo(200))
            .body("message", equalTo("User created width success"))
            .body("data", notNullValue())
            .body("data.firstname", equalTo(registerDTO.firstname()))
            .body("data.lastname", equalTo(registerDTO.lastname()))
            .body("data.email", equalTo(registerDTO.email()))
            .body("data.role", equalTo(registerDTO.role().name()))
            .body("data.enabled", equalTo(true));
    }

    @ParameterizedTest
    @MethodSource("provideRegisterDtoForTesting")
    void should_throws_exception_when_signup_with_invalid_data(RegisterDTO registerDTO, String jsonPathMessage, String jsonPathData) throws JsonProcessingException {

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(registerDTO)).
                when()
                .post(API_AUTH_URL.concat("signup")).
                then()
                .statusCode(400)
                .body("flag", is(false))
                .body("code", is(400))
                .body("message", is("Provided arguments are invalid, see data for details."))
                .body("data.'"+ jsonPathData + "'", is(jsonPathMessage));
    }

    @Test
    void should_throws_exception_when_signup_with_same_email() throws JsonProcessingException {

        RegisterDTO registerDTO = getRegisterDTO("Ange Carmel", "YORO","yoro1@gmail.com", "codeur47", Role.USER);
        String jsonRequest = objectMapper.writeValueAsString(registerDTO);

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(jsonRequest).
                when()
                .post(API_AUTH_URL.concat("signup"));

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(jsonRequest).
                when()
                .post(API_AUTH_URL.concat("signup")).
                then()
                .body("flag", is(false))
                .body("code", is(400))
                .body("data", is("Email yoro1@gmail.com est déjà utilisé."));
    }

    @Test
    void should_login_when_login_width_existing_user() throws JsonProcessingException {

        RegisterDTO registerDTO = getRegisterDTO("Martial", "GARCIA","garcian@gmail.com", "codeur47", Role.USER);

        this.authService.signup(registerDTO);

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(getLoginRequestDTO(registerDTO.email(), registerDTO.password()))).
                when()
                .post(API_AUTH_URL.concat("login")).
                then()
                .body("flag", is(true))
                .body("code", is(200))
                .body("message", is("User login width success"))
                .body("data", notNullValue())
                .body("data.username", equalTo("garcian@gmail.com"));
    }

    @ParameterizedTest
    @MethodSource("provideLoginRequestDTOForTesting")
    void should_throws_exception_when_login_with_invalid_data(LoginRequestDTO loginRequestDTO, String expectedMessage, String jsonPathData) throws JsonProcessingException {

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(loginRequestDTO)).
                when()
                .post(API_AUTH_URL.concat("login")).
                then()
                .statusCode(400)
                .body("flag", is(false))
                .body("code", is(400))
                .body("message", is("Provided arguments are invalid, see data for details."))
                .body("data.'"+ jsonPathData + "'", is(expectedMessage));
    }

}
