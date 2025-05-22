package ru.mirea.auth.service.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ErrorResponseDto {
    private String message;
    private OffsetDateTime time;

    public ErrorResponseDto(String message) {
        this.message = message;
        this.time = OffsetDateTime.now();
    }
}
