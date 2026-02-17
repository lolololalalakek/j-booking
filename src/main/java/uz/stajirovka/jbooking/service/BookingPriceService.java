package uz.stajirovka.jbooking.service;

import uz.stajirovka.jbooking.entity.RoomEntity;

// сервис расчёта стоимости бронирования
public interface BookingPriceService {

    // рассчитывает общую стоимость бронирования
    Long calculate(RoomEntity room, long nights);
}
