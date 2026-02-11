package uz.stajirovka.jbooking.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record GuestInfoRequest(
        @NotBlank(message = "Имя обязательно")
        String firstName,

        @NotBlank(message = "Фамилия обязательна")
        String lastName,

        // Email опционален, но если указан - должен быть валидным
        @Email(message = "Неверный формат email")
        String email,

        String phone
) {}
