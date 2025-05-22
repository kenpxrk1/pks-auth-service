package ru.mirea.auth.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private String accessToken;
    private UUID refreshToken;
}
