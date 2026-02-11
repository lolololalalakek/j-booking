package uz.stajirovka.jbooking.constant.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Error {

    // Внутренние ошибки (10xxx)
    INTERNAL_ERROR(10001, "Внутренняя ошибка сервера"),
    HANDLER_NOT_FOUND(10002, "Обработчик не найден"),
    METHOD_NOT_SUPPORTED(10003, "Метод не поддерживается"),

    // Ошибки валидации (20xxx)
    VALIDATION_ERROR(20001, "Ошибка валидации"),
    INVALID_REQUEST_PARAM(20002, "Неверный параметр запроса"),
    JSON_NOT_VALID(20003, "Невалидный JSON"),
    MISSING_REQUEST_HEADER(20004, "Отсутствует заголовок запроса"),

    // Ошибки бизнес-логики - Not Found (30xxx)
    BOOKING_NOT_FOUND(30001, "Бронирование не найдено"),
    CITY_NOT_FOUND(30002, "Город не найден"),
    HOTEL_NOT_FOUND(30003, "Отель не найден"),
    ROOM_NOT_FOUND(30004, "Номер не найден"),
    GUEST_NOT_FOUND(30005, "Гость не найден"),
    REVIEW_NOT_FOUND(30006, "Отзыв не найден"),

    // Ошибки бизнес-логики - Конфликты (40xxx)
    ROOM_NOT_AVAILABLE(40001, "Номер недоступен на выбранные даты"),
    INVALID_BOOKING_STATUS(40002, "Недопустимый статус бронирования"),
    DATA_INTEGRITY_VIOLATION(40003, "Нарушение целостности данных"),
    OVERLAPPING_BOOKING(40004, "Номер уже забронирован на эти даты"),
    GUEST_ALREADY_EXISTS(40005, "Гость с таким email уже существует"),
    REVIEW_ALREADY_EXISTS(40006, "Отзыв от этого гостя уже существует"),
    CITY_HAS_HOTELS(40007, "Невозможно удалить город с активными отелями"),
    HOTEL_HAS_ROOMS(40008, "Невозможно удалить отель с активными номерами"),
    ROOM_HAS_BOOKINGS(40009, "Невозможно удалить номер с активными бронированиями"),
    ROOM_CAPACITY_EXCEEDED(40010, "Количество гостей превышает вместимость номера");

    private final int code;
    private final String message;
}
