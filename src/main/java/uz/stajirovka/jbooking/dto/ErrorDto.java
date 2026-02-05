package uz.stajirovka.jbooking.dto;

import java.time.LocalDateTime;

public record ErrorDto(
        int status,
        String message,
        LocalDateTime timestamp
) {
}
