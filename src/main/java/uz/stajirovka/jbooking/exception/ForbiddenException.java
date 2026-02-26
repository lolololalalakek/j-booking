package uz.stajirovka.jbooking.exception;

import org.springframework.http.HttpStatus;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.constant.enums.ErrorType;

public class ForbiddenException extends BaseException {

    public ForbiddenException(Error error) {
        super(error, HttpStatus.FORBIDDEN, ErrorType.BUSINESS);
    }
}
