package uz.stajirovka.jbooking.dto.response;

import java.time.LocalDateTime;

public record GuestResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        LocalDateTime createdAt
) {}
