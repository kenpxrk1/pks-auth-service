package ru.mirea.auth.service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class RefreshTokenRequestDto {
    @NotNull(message = "Refresh token must not be null")
    private UUID refreshToken;
}
