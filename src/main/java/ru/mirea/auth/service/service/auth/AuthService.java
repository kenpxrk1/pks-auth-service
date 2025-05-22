package ru.mirea.auth.service.service.auth;

import ru.mirea.auth.service.dto.request.LoginRequestDto;
import ru.mirea.auth.service.dto.request.RefreshTokenRequestDto;
import ru.mirea.auth.service.dto.request.RegisterRequestDto;
import ru.mirea.auth.service.dto.response.LoginResponseDto;
import ru.mirea.auth.service.dto.response.RefreshTokenResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginDto);
    void register(RegisterRequestDto registerDto);
    RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto refreshDto);
}
