package ru.mirea.auth.service.repository;

import ru.mirea.auth.service.model.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    void deleteByExpireTimeIsBefore(OffsetDateTime expireTime);
    Optional<TokenEntity> findByRefreshToken(UUID refreshToken);
}
