package ru.mirea.auth.service.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;
import ru.mirea.auth.service.dto.event.UserCreateEventDto;
import ru.mirea.auth.service.dto.response.LoginResponseDto;
import ru.mirea.auth.service.repository.AccountRepository;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
public class AuthControllerTest extends AbstractInitialization{

    @Container
    protected static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withInitScript("init-schema.sql");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp(){
        accountRepository.deleteAll();

        doNothing().when(userRegistrationProducer)
                .sendUserCreatedEvent(any(UserCreateEventDto.class));
    }

    @Test
    void signUp_successful() throws Exception {
        mvc.perform(post(BASE_URL + "sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(correctRegJson)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void signUp_emptyName() throws Exception{
        mvc.perform(post(BASE_URL + "sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyNameRegJson)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_emptySecondName() throws Exception{
        mvc.perform(post(BASE_URL + "sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptySecondNameRegJson)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_invalidPhoneNumber() throws Exception {
        mvc.perform(post(BASE_URL + "sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shortNumberRegJson)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_invalidPassword() throws Exception{
        mvc.perform(post(BASE_URL + "sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shortPasswordRegJson)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void singUp_invalidDate() throws Exception{
        mvc.perform(post(BASE_URL + "sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidDateOfBirthRegJson)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_success() throws Exception{
        mvc.perform(post(BASE_URL + "sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(correctRegJson)
                )
                .andExpect(status().isCreated());

        mvc.perform(post(BASE_URL + "sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(correctLoginJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void login_noSuchPhoneNumber() throws Exception{
        mvc.perform(post(BASE_URL + "sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(correctRegJson)
                )
                .andExpect(status().isCreated());

        mvc.perform(post(BASE_URL + "sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(noSuchNumberLoginJson)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_wrongPass() throws Exception{
        mvc.perform(post(BASE_URL + "sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(correctRegJson)
                )
                .andExpect(status().isCreated());

        mvc.perform(post(BASE_URL + "sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wrongPassLoginJson)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_shortPhoneNumber() throws Exception{
        mvc.perform(post(BASE_URL + "sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(correctRegJson)
                )
                .andExpect(status().isCreated());

        mvc.perform(post(BASE_URL + "sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shortNumberLoginJson)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_shortPassword() throws Exception{
        mvc.perform(post(BASE_URL + "sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(correctRegJson)
                )
                .andExpect(status().isCreated());

        mvc.perform(post(BASE_URL + "sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shortPasswordLoginJson)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void refresh_success() throws Exception{
        mvc.perform(post(BASE_URL + "sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(correctRegJson)
                );

        MvcResult mvcResult = mvc.perform(post(BASE_URL + "sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(correctLoginJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andReturn();

        UUID refreshToken = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), LoginResponseDto.class).getRefreshToken();

        String correctJson = String.format("""
                {
                    "refreshToken": "%s"
                }
                """, refreshToken);

        mvc.perform(post(BASE_URL + "refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(correctJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void refresh_emptyRefreshToken() throws Exception {
        mvc.perform(post(BASE_URL + "refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyRefreshTokenJson)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void refresh_noSuchToken() throws Exception{
        mvc.perform(post(BASE_URL + "refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(noSuchRefreshTokenJson)
                )
                .andExpect(status().isNotFound());
    }
}
