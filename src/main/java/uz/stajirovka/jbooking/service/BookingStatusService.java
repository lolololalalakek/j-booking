package uz.stajirovka.jbooking.service;

import uz.stajirovka.jbooking.dto.request.BookingModifyRequest;
import uz.stajirovka.jbooking.dto.response.BookingResponse;

import java.math.BigDecimal;

// сервис управления статусами бронирования
public interface BookingStatusService {


    // отмена бронирования (HOLD/CONFIRMED/PAID -> CANCELLED)
    BookingResponse cancel(Long bookingId);

    // изменение бронирования (CONFIRMED/PAID -> MODIFIED)
    BookingResponse modify(Long bookingId, BookingModifyRequest request);

    // получить сумму возврата при отмене
    BigDecimal getRefundAmount(Long bookingId);

    // получить процент возврата при отмене (0-100)
    int getRefundPercent(Long bookingId);
}
