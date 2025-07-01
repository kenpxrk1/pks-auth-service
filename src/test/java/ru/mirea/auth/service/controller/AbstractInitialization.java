package ru.mirea.auth.service.controller;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.mirea.auth.service.kafka.UserRegistrationProducer;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class AbstractInitialization {
    @Autowired
    protected MockMvc mvc;
    @MockitoBean
    protected UserRegistrationProducer userRegistrationProducer;
    protected static String BASE_URL = "http://localhost:8081/api/v1/auth/";
    protected static String correctRegJson;
    protected static String emptyNameRegJson;
    protected static String emptySecondNameRegJson;
    protected static String shortNumberRegJson;
    protected static String shortPasswordRegJson;
    protected static String invalidDateOfBirthRegJson;
    protected static String correctLoginJson;
    protected static String wrongPassLoginJson;
    protected static String noSuchNumberLoginJson;
    protected static String shortNumberLoginJson;
    protected static String shortPasswordLoginJson;
    protected static String emptyRefreshTokenJson;
    protected static String noSuchRefreshTokenJson;

    @BeforeAll
    public static void init(){
        correctRegJson = """
            {
                "firstName": "Сергей",
                "lastName": "Боженков",
                "phoneNumber": "81231320981",
                "password": "somePassword",
                "dateOfBirth": "2004-02-21"
            }
            """;
        emptyNameRegJson = """
            {
                "firstName": "",
                "lastName": "Боженков",
                "phoneNumber": "81231320981",
                "password": "somePassword",
                "dateOfBirth": "2004-02-21"
            }
            """;
        emptySecondNameRegJson = """
            {
                "firstName": "Сергей",
                "lastName": "",
                "phoneNumber": "81231320981",
                "password": "somePassword",
                "dateOfBirth": "2004-02-21"
            }
            """;
        shortNumberRegJson = """
            {
                "firstName": "Сергей",
                "lastName": "Боженков",
                "phoneNumber": "83181",
                "password": "somePassword",
                "dateOfBirth": "2004-02-21"
            }
            """;
        shortPasswordRegJson = """
            {
                "firstName": "Сергей",
                "lastName": "Боженков",
                "phoneNumber": "81231320981",
                "password": "1212",
                "dateOfBirth": "2004-02-21"
            }
            """;
        Date date = Date.from(Instant.now().plusSeconds(1000000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        invalidDateOfBirthRegJson = String.format("""
            {
                "firstName": "Сергей",
                "lastName": "Боженков",
                "phoneNumber": "81231320981",
                "password": "1212",
                "dateOfBirth": "%s"
            }
            """, dateStr);
        correctLoginJson = """
            {
                "phoneNumber": "81231320981",
                "password": "somePassword"
            }
            """;
        noSuchNumberLoginJson = """
            {
                "phoneNumber": "81231234567",
                "password": "somePassword"
            }
            """;
        wrongPassLoginJson ="""
            {
                "phoneNumber": "81231320981",
                "password": "otherPassword"
            }
            """;
        shortNumberLoginJson = """
            {
                "phoneNumber": "123",
                "password": "somePassword"
            }
            """;
        shortPasswordLoginJson = """
            {
                "phoneNumber": "81231320981",
                "password": "short"
            }
            """;
        emptyRefreshTokenJson = """
            {
                "refreshToken": ""
            }
            """;
        UUID nonExistentRefreshToken = UUID.randomUUID();
        noSuchRefreshTokenJson = String.format("""
            {
                "refreshToken": "%s"
            }
            """, nonExistentRefreshToken);
    }
}
