package uz.stajirovka.jbooking.service;

import uz.stajirovka.jbooking.entity.BookingEntity;

import java.time.LocalDateTime;

// сервис политики отмены бронирования
public interface CancellationPolicyService {

    // рассчитывает сумму возврата при отмене
    Long calculateRefund(BookingEntity booking);

    // возвращает процент возврата (0-100)
    int getRefundPercent(LocalDateTime checkInDate);
}
