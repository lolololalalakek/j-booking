package uz.stajirovka.jbooking.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record BookingModifyRequest(
        @NotNull(message = "ID города обязателен")
        Long cityId,

        @NotNull(message = "ID отеля обязателен")
        Long hotelId,

        @NotNull(message = "ID номера обязателен")
        Long roomId,

        @NotNull(message = "Дата заезда обязательна")
        @Future(message = "Дата заезда должна быть в будущем")
        LocalDateTime checkInDate,

        @NotNull(message = "Дата выезда обязательна")
        @Future(message = "Дата выезда должна быть в будущем")
        LocalDateTime checkOutDate
) {}
