package uz.stajirovka.jbooking.dto.request;

public record BookingConfirmRequest(
    Long bookingId,

    Long cardId

    //при подтверждении букинга на сайте выбирается карта которая будет производить оплату


) {


}
