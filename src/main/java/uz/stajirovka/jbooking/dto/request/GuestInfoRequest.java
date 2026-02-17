package uz.stajirovka.jbooking.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record GuestInfoRequest(
        @NotBlank(message = "Имя обязательно")
        String firstName,

        @NotBlank(message = "Фамилия обязательна")
        String lastName,

        @NotBlank(message = "ПИНФЛ обязателен")
        @Pattern(regexp = "\\d{14}", message = "ПИНФЛ должен содержать 14 цифр")
        String pinfl,

        @Email(message = "Неверный формат email")
        String email,

        String phone
) {}
