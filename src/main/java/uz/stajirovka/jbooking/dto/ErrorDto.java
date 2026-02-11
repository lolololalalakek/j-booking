package uz.stajirovka.jbooking.dto;

import lombok.Builder;
import uz.stajirovka.jbooking.constant.enums.ErrorType;

import java.util.List;

@Builder
public record ErrorDto(
        int code,
        String message,
        ErrorType type,
        List<String> validationErrors
) {}
