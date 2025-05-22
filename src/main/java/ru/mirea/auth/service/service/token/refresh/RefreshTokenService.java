package ru.mirea.auth.service.service.token.refresh;

import java.util.UUID;

public interface RefreshTokenService {
    UUID generateRefreshToken();
    void cleanupExpiredTokens();
}
