package ru.mirea.auth.service.service.token.refresh;

import ru.mirea.auth.service.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final TokenRepository tokenRepository;

    @Override
    public UUID generateRefreshToken() {
        return UUID.randomUUID();
    }


    @Scheduled(fixedRateString = "${scheduling.fixed-rate}")
    @Transactional
    public void cleanupExpiredTokens() {
        log.info("Cleaning up expired tokens");
        tokenRepository.deleteByExpireTimeIsBefore(OffsetDateTime.now());
    }
}
