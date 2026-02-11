package uz.stajirovka.jbooking.exception;

import org.springframework.http.HttpStatus;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.constant.enums.ErrorType;

public class NotFoundException extends BaseException {

    public NotFoundException(Error error) {
        super(error, HttpStatus.NOT_FOUND, ErrorType.BUSINESS);
    }

    public NotFoundException(Error error, String details) {
        super(error, details, HttpStatus.NOT_FOUND, ErrorType.BUSINESS);
    }
}
