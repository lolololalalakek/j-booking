package uz.stajirovka.jbooking.service.impl;

import org.springframework.stereotype.Service;
import uz.stajirovka.jbooking.entity.RoomEntity;
import uz.stajirovka.jbooking.service.BookingPriceService;

@Service
public class BookingPriceServiceImpl implements BookingPriceService {

    //сервис для подсчета цены - умножает цену за ночь на кол-во ночей
    @Override
    public Long calculate(RoomEntity room, long nights) {
        return room.getPricePerNight() * nights;
    }
}
