package ru.mirea.auth.service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {
    @NotEmpty(message = "Name must not be empty")
    private String firstName;
    @NotEmpty(message = "Second name must not be empty")
    private String lastName;
    @Size(min = 11, max = 11, message = "Phone number length must be 11 characters")
    private String phoneNumber;
    @Size(min = 8, message = "Password must be at least 8 characters length")
    private String password;
    @Past(message = "Date of birth must be in past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
}
