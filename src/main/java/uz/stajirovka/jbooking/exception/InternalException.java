package uz.stajirovka.jbooking.exception;

import org.springframework.http.HttpStatus;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.constant.enums.ErrorType;

public class InternalException extends BaseException {

    public InternalException(Error error) {
        super(error, HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.INTERNAL);
    }

    public InternalException(Error error, String details) {
        super(error, details, HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.INTERNAL);
    }
}
