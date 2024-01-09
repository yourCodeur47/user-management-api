package io.yorosoft.usermanagementapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.yorosoft.usermanagementapi.config.TestConfigurer;
import io.yorosoft.usermanagementapi.dto.RegisterDTO;
import io.yorosoft.usermanagementapi.enums.Role;
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
import static org.hamcrest.Matchers.is;

@Testcontainers
@ExtendWith(SpringExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerIntegrationTest extends TestConfigurer {

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

        RegisterDTO registerDTO = new RegisterDTO("Ange Carmel", "YORO","yoro@gmail.com", "codeur47", Role.USER);
        String jsonRequest = objectMapper.writeValueAsString(registerDTO);

        given()
            .contentType(ContentType.JSON)
            .body(jsonRequest)
            .when()
            .post("/api/auth")
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
            .body("data.enabled", equalTo(false));
    }

    @ParameterizedTest
    @MethodSource("provideRegisterDtoForTesting")
    void should_throws_exception_when_signup_with_invalid_data(RegisterDTO registerDTO, String jsonPathMessage, String jsonPathData) throws JsonProcessingException {

        String jsonRequest = objectMapper.writeValueAsString(registerDTO);

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(jsonRequest).
                when()
                .post("/api/auth").
                then()
                .statusCode(400)
                .body("flag", is(false))
                .body("code", is(400))
                .body("message", is("Provided arguments are invalid, see data for details."))
                .body("data.'"+ jsonPathData + "'", is(jsonPathMessage));
    }

    @Test
    void should_throws_exception_when_signup_with_same_email() throws JsonProcessingException {

        RegisterDTO registerDTO = new RegisterDTO("Ange Carmel", "YORO","yoro@gmail.com", "codeur47", Role.USER);
        String jsonRequest = objectMapper.writeValueAsString(registerDTO);

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(jsonRequest).
                when()
                .post("/api/auth");

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(jsonRequest).
                when()
                .post("/api/auth").
                then()
                .body("flag", is(false))
                .body("code", is(400))
                .body("data", is("Email yoro@gmail.com est déjà utilisé."));
    }

}
