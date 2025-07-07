package ru.mirea.auth.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Auth controller", description = "operations to authenticate user")
public class AuthController {
    private final AuthService authService;

    @PostMapping("sign-up")
    @Operation(
            summary = "Register user by dto",
            description = "Returns 'created' status",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Incorrect dto"),
                    @ApiResponse(responseCode = "409", description = "Account with phoneNumber *** already exists")
            })
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDto registerDto) {
        authService.register(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("sign-in")
    @Operation(
            summary = "Login user by dto",
            description = "Returns 'ok' status with refresh and access tokens",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Incorrect dto"),
                    @ApiResponse(responseCode = "409", description = "Account with login *** not exists")
            })
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @PostMapping("refresh")
    @Operation(
            summary = "Get new access and refresh tokens by refresh token",
            description = "Returns 'ok' status with refresh and access keys",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Incorrect dto"),
                    @ApiResponse(responseCode = "404", description = "Invalid token")
            })
    public ResponseEntity<RefreshTokenResponseDto> refresh(@RequestBody @Valid RefreshTokenRequestDto requestDto) {
        return ResponseEntity.ok(authService.refreshToken(requestDto));
    }
}
