package ru.mirea.auth.service.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
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
public class AuthControllerTest extends AbstractInitialization {

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void setUp() {
        accountRepository.deleteAll();

        doNothing().when(userRegistrationProducer)
                .sendUserCreatedEvent(any(UserCreateEventDto.class));
    }

    @Test
    public void signUp_successful() throws Exception {
        mvc.perform(post(BASE_URL + "sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(correctRegJson)
                )
                .andExpect(status().isCreated());
    }

    @Test
    public void signUp_emptyName() throws Exception {
        mvc.perform(post(BASE_URL + "sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyNameRegJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").exists())
                .andExpect(jsonPath("$[0].message")
                        .value("Name must not be empty"));
    }

    @Test
    public void signUp_emptySecondName() throws Exception {
        mvc.perform(post(BASE_URL + "sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptySecondNameRegJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").exists())
                .andExpect(jsonPath("$[0].message")
                        .value("Second name must not be empty"));
    }

    @Test
    public void signUp_invalidPhoneNumber() throws Exception {
        mvc.perform(post(BASE_URL + "sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shortNumberRegJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").exists())
                .andExpect(jsonPath("$[0].message")
                        .value("Phone number length must be 11 characters"));
    }

    @Test
    public void signUp_invalidPassword() throws Exception {
        mvc.perform(post(BASE_URL + "sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shortPasswordRegJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").exists())
                .andExpect(jsonPath("$[0].message")
                        .value("Password must be at least 8 characters length"));
    }

    @Test
    public void singUp_invalidDate() throws Exception {
        mvc.perform(post(BASE_URL + "sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidDateOfBirthRegJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").exists())
                .andExpect(jsonPath("$[0].message")
                        .value("Date of birth must be in past"));
    }

    @Test
    public void signUp_numberAlreadyExists() throws Exception {
        mvc.perform(post(BASE_URL + "sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(correctRegJson)
                )
                .andExpect(status().isCreated());

        mvc.perform(post(BASE_URL + "sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(correctRegJson)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message")
                        .value("Account with phoneNumber " + correctPhoneNumber + " already exists"));
    }

    @Test
    public void login_success() throws Exception {
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
    public void login_noSuchPhoneNumber() throws Exception {
        mvc.perform(post(BASE_URL + "sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(correctLoginJson)
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message")
                        .value("Account with login '" + correctPhoneNumber + "' not found"));
    }

    @Test
    public void login_wrongPass() throws Exception {
        mvc.perform(post(BASE_URL + "sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(correctRegJson)
                )
                .andExpect(status().isCreated());

        mvc.perform(post(BASE_URL + "sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wrongPassLoginJson)
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message")
                        .value("Bad credentials"));
    }

    @Test
    public void login_shortPhoneNumber() throws Exception {
        mvc.perform(post(BASE_URL + "sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(correctRegJson)
                )
                .andExpect(status().isCreated());

        mvc.perform(post(BASE_URL + "sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shortNumberLoginJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").exists())
                .andExpect(jsonPath("$[0].message")
                        .value("Phone number length must be 11 characters"));

    }

    @Test
    public void login_shortPassword() throws Exception {
        mvc.perform(post(BASE_URL + "sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(correctRegJson)
                )
                .andExpect(status().isCreated());

        mvc.perform(post(BASE_URL + "sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shortPasswordLoginJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").exists())
                .andExpect(jsonPath("$[0].message")
                        .value("Password must be at least 8 characters length"));
    }

    @Test
    public void refresh_success() throws Exception {
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
    public void refresh_emptyRefreshToken() throws Exception {
        mvc.perform(post(BASE_URL + "refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyRefreshTokenJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").exists())
                .andExpect(jsonPath("$[0].message")
                        .value("Refresh token must not be null"));
    }

    @Test
    public void refresh_noSuchToken() throws Exception {
        mvc.perform(post(BASE_URL + "refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(noSuchRefreshTokenJson)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message")
                        .value("Token '" + noSuchRefreshToken + "' is invalid"));
    }
}
