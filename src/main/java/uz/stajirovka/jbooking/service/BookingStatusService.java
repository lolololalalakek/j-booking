package uz.stajirovka.jbooking.service;

import uz.stajirovka.jbooking.dto.request.BookingModifyRequest;
import uz.stajirovka.jbooking.dto.response.BookingResponse;

// сервис управления статусами бронирования
public interface BookingStatusService {

    // отмена бронирования (HOLD/CONFIRMED/MODIFIED -> CANCELLED)
    BookingResponse cancel(Long bookingId);

    // изменение бронирования (HOLD/CONFIRMED/MODIFIED -> MODIFIED)
    BookingResponse modify(Long bookingId, BookingModifyRequest request);

    // получить сумму возврата при отмене
    Long getRefundAmount(Long bookingId);

    // получить процент возврата при отмене (0-100)
    int getRefundPercent(Long bookingId);
}
