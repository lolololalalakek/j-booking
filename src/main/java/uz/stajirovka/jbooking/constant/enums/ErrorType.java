package uz.stajirovka.jbooking.constant.enums;

public enum ErrorType {
    VALIDATION,  // Ошибки валидации входных данных
    BUSINESS,    // Ошибки бизнес-логики (not found, conflict)
    INTERNAL,    // Внутренние ошибки сервера
    EXTERNAL     // Ошибки внешних сервисов
}
