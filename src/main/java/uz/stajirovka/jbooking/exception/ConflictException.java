package uz.stajirovka.jbooking.exception;

import org.springframework.http.HttpStatus;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.constant.enums.ErrorType;

public class ConflictException extends BaseException {

    public ConflictException(Error error) {
        super(error, HttpStatus.CONFLICT, ErrorType.BUSINESS);
    }

    public ConflictException(Error error, String details) {
        super(error, details, HttpStatus.CONFLICT, ErrorType.BUSINESS);
    }
}
