package ru.mirea.auth.service.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    @Size(min = 11, max = 11, message = "Phone number length must be 11 characters")
    private String phoneNumber;
    @Size(min = 8, message = "Password must be at least 8 characters length")
    private String password;
}
