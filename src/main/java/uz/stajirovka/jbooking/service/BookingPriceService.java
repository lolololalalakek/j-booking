package uz.stajirovka.jbooking.service;

import uz.stajirovka.jbooking.entity.RoomEntity;

import java.math.BigDecimal;

// сервис расчёта стоимости бронирования
public interface BookingPriceService {

    // рассчитывает общую стоимость бронирования
    BigDecimal calculate(RoomEntity room, long nights);
}
