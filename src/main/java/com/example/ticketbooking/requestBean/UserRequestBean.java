package com.example.ticketbooking.requestBean;

import com.example.ticketbooking.enum_package.Gender;
import jakarta.validation.constraints.*;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequestBean {

    @NotBlank(message = "Username is required")
    private String username;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "\\d{10}", message = "Mobile must be a 10-digit number")
    @NotBlank(message = "Mobile number is required")
    private String mobile;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @Min(value = 0, message = "Age must be non-negative")
    private int age;
}
