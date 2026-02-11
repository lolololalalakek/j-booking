package uz.stajirovka.jbooking.dto.response;


import uz.stajirovka.jbooking.constant.enums.BookingStatus;

public record BookingConfirmResponse(
    BookingStatus bookingStatus,
    Long bookingId,
    Long transactionId
) {

}
