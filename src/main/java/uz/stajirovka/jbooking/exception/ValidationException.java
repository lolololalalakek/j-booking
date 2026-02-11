package uz.stajirovka.jbooking.exception;

import org.springframework.http.HttpStatus;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.constant.enums.ErrorType;

public class ValidationException extends BaseException {

    public ValidationException(Error error) {
        super(error, HttpStatus.BAD_REQUEST, ErrorType.VALIDATION);
    }

    public ValidationException(Error error, String details) {
        super(error, details, HttpStatus.BAD_REQUEST, ErrorType.VALIDATION);
    }
}
