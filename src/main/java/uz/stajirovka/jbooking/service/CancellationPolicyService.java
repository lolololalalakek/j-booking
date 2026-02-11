package uz.stajirovka.jbooking.service;

import uz.stajirovka.jbooking.entity.BookingEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// сервис политики отмены бронирования
public interface CancellationPolicyService {

    // рассчитывает сумму возврата при отмене
    BigDecimal calculateRefund(BookingEntity booking);

    // возвращает процент возврата (0-100)
    int getRefundPercent(LocalDateTime checkInDate);
}
