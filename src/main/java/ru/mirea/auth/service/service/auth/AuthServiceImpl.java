package ru.mirea.auth.service.service.auth;

import ru.mirea.auth.service.dto.request.LoginRequestDto;
import ru.mirea.auth.service.dto.request.RefreshTokenRequestDto;
import ru.mirea.auth.service.dto.request.RegisterRequestDto;
import ru.mirea.auth.service.dto.response.LoginResponseDto;
import ru.mirea.auth.service.dto.response.RefreshTokenResponseDto;
import ru.mirea.auth.service.exception.EntityAlreadyExistsException;
import ru.mirea.auth.service.exception.KafkaPublishException;
import ru.mirea.auth.service.kafka.UserRegistrationProducer;
import ru.mirea.auth.service.mapper.AccountMapper;
import ru.mirea.auth.service.mapper.TokenMapper;
import ru.mirea.auth.service.model.AccountEntity;
import ru.mirea.auth.service.model.TokenEntity;
import ru.mirea.auth.service.repository.TokenRepository;
import ru.mirea.auth.service.security.details.AccountDetails;
import ru.mirea.auth.service.service.account.AccountService;
import ru.mirea.auth.service.service.token.JwtService;
import ru.mirea.auth.service.service.token.refresh.RefreshTokenService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${jwt.expire-time-refresh-token}")
    private Long expireTime;

    private final AccountService accountService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final TokenRepository tokenRepository;
    private final AccountMapper mapper;
    private final TokenMapper tokenMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final UserRegistrationProducer userRegistrationProducer;

    @Transactional
    @Override
    public LoginResponseDto login(LoginRequestDto loginDto) {
        log.info("Handle authenticate request for phone_number={}", loginDto.getPhoneNumber());

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getPhoneNumber(), loginDto.getPassword()));

        AccountDetails account = (AccountDetails) auth.getPrincipal();

        log.debug("Generating tokens");
        String accessToken = jwtService.generateAccessToken(loginDto.getPhoneNumber(), account.getAccount().getRole());
        UUID refreshToken = refreshTokenService.generateRefreshToken();
        log.debug("Tokens successfully created");

        TokenEntity token = tokenMapper.toTokenEntity(account.getAccount(), refreshToken);
        token.setExpireTime(OffsetDateTime.now().plusHours(expireTime));

        tokenRepository.save(token);

        return tokenMapper.toLoginResponse(accessToken, refreshToken);
    }

    @Transactional
    @Override
    public void register(RegisterRequestDto registerDto) {
        log.info("Handle registry request for phone_number={}", registerDto.getPhoneNumber());

        if (accountService.existsByPhoneNumber(registerDto.getPhoneNumber())) {
            throw new EntityAlreadyExistsException(
                    "Account with phoneNumber " + registerDto.getPhoneNumber() + " already exists");
        }

        AccountEntity accountEntity = mapper.toEntity(registerDto, passwordEncoder);

        accountService.save(accountEntity);

        log.info("User with phone_number={} successfully registered", accountEntity.getPhoneNumber());

        try {
            userRegistrationProducer.sendUserCreatedEvent(
                    mapper.toUserCreatedEventDto(accountEntity, registerDto)
            );
        } catch (Exception e) {
            log.error("Failed to send event to Kafka. Rolling back user registration", e);
            throw new KafkaPublishException("User registration failed due to Kafka outage");
        }
    }


    @Transactional
    @Override
    public RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto refreshDto) {
        log.info("refreshing token");
        TokenEntity tokenToRefresh = tokenRepository.findByRefreshToken(refreshDto.getRefreshToken()).orElseThrow(
                        () -> new EntityNotFoundException("Token '" + refreshDto.getRefreshToken() + "' is invalid"));
        AccountEntity account = tokenToRefresh.getAccount();
        UUID refreshToken = refreshTokenService.generateRefreshToken();
        tokenToRefresh.setRefreshToken(refreshToken);
        tokenToRefresh.setExpireTime(OffsetDateTime.now().plusHours(expireTime));
        String accessToken = jwtService.generateAccessToken(account.getPhoneNumber(), account.getRole());
        return tokenMapper.toRefreshReponse(accessToken, refreshToken);
    }
}
