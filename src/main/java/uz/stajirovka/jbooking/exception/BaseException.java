package uz.stajirovka.jbooking.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.constant.enums.ErrorType;

@Getter
public abstract class BaseException extends RuntimeException {

    private final int code;
    private final String message;
    private final HttpStatus status;
    private final ErrorType errorType;

    protected BaseException(int code, String message, HttpStatus status, ErrorType errorType) {
        super(message);
        this.code = code;
        this.message = message;
        this.status = status;
        this.errorType = errorType;
    }

    protected BaseException(Error error, HttpStatus status, ErrorType errorType) {
        this(error.getCode(), error.getMessage(), status, errorType);
    }

    protected BaseException(Error error, String details, HttpStatus status, ErrorType errorType) {
        super(error.getMessage() + ": " + details);
        this.code = error.getCode();
        this.message = error.getMessage() + ": " + details;
        this.status = status;
        this.errorType = errorType;
    }
}
