package uz.stajirovka.jbooking.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.constant.enums.ErrorType;
import uz.stajirovka.jbooking.dto.ErrorDto;
import uz.stajirovka.jbooking.exception.BaseException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Бизнес-исключения (BaseException и все наследники)
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorDto> handleBaseException(BaseException ex) {
        log.warn("Бизнес-ошибка: {} - {}", ex.getCode(), ex.getMessage());
        return ResponseEntity.status(ex.getStatus())
                .body(ErrorDto.builder()
                        .code(ex.getCode())
                        .message(ex.getMessage())
                        .type(ex.getErrorType())
                        .build());
    }

    // Ошибки валидации полей (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();
        log.debug("Ошибка валидации полей: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorDto.builder()
                        .code(Error.VALIDATION_ERROR.getCode())
                        .message(Error.VALIDATION_ERROR.getMessage())
                        .type(ErrorType.VALIDATION)
                        .validationErrors(errors)
                        .build());
    }

    // Невалидный JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("Невалидный JSON: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorDto.builder()
                        .code(Error.JSON_NOT_VALID.getCode())
                        .message(Error.JSON_NOT_VALID.getMessage())
                        .type(ErrorType.VALIDATION)
                        .build());
    }

    // Неверный тип параметра
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.warn("Неверный параметр: {} = {}", ex.getName(), ex.getValue());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorDto.builder()
                        .code(Error.INVALID_REQUEST_PARAM.getCode())
                        .message(Error.INVALID_REQUEST_PARAM.getMessage() + ": " + ex.getName())
                        .type(ErrorType.VALIDATION)
                        .build());
    }

    // Отсутствует заголовок
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorDto> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        log.warn("Отсутствует заголовок: {}", ex.getHeaderName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorDto.builder()
                        .code(Error.MISSING_REQUEST_HEADER.getCode())
                        .message(Error.MISSING_REQUEST_HEADER.getMessage() + ": " + ex.getHeaderName())
                        .type(ErrorType.VALIDATION)
                        .build());
    }

    // Метод не поддерживается
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorDto> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.warn("Метод не поддерживается: {}", ex.getMethod());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ErrorDto.builder()
                        .code(Error.METHOD_NOT_SUPPORTED.getCode())
                        .message(Error.METHOD_NOT_SUPPORTED.getMessage() + ": " + ex.getMethod())
                        .type(ErrorType.VALIDATION)
                        .build());
    }

    // Обработчик не найден
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDto> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        log.warn("Обработчик не найден: {} {}", ex.getHttpMethod(), ex.getRequestURL());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorDto.builder()
                        .code(Error.HANDLER_NOT_FOUND.getCode())
                        .message(Error.HANDLER_NOT_FOUND.getMessage())
                        .type(ErrorType.INTERNAL)
                        .build());
    }

    // Нарушение целостности данных (UNIQUE, FK, exclusion constraint)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDto> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.warn("Нарушение целостности данных: {}", ex.getMessage());

        Error error = Error.DATA_INTEGRITY_VIOLATION;
        if (ex.getMessage() != null && ex.getMessage().contains("no_overlapping_bookings")) {
            error = Error.OVERLAPPING_BOOKING;
        }

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorDto.builder()
                        .code(error.getCode())
                        .message(error.getMessage())
                        .type(ErrorType.VALIDATION)
                        .build());
    }

    // Все остальные исключения
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        log.error("Внутренняя ошибка сервера", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorDto.builder()
                        .code(Error.INTERNAL_ERROR.getCode())
                        .message(Error.INTERNAL_ERROR.getMessage())
                        .type(ErrorType.INTERNAL)
                        .build());
    }
}
