package ru.mirea.auth.service.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class RefreshTokenRequestDto {
    private UUID refreshToken;
}
