package ru.mirea.auth.service.controller;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import ru.mirea.auth.service.dto.request.LoginRequestDto;
import ru.mirea.auth.service.dto.request.RefreshTokenRequestDto;
import ru.mirea.auth.service.dto.request.RegisterRequestDto;
import ru.mirea.auth.service.dto.response.LoginResponseDto;
import ru.mirea.auth.service.dto.response.RefreshTokenResponseDto;
import ru.mirea.auth.service.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("sign-up")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDto registerDto) {
        authService.register(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("sign-in")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @PostMapping("refresh")
    public ResponseEntity<RefreshTokenResponseDto> refresh(@RequestBody @Valid RefreshTokenRequestDto requestDto) {
        return ResponseEntity.ok(authService.refreshToken(requestDto));
    }
}
