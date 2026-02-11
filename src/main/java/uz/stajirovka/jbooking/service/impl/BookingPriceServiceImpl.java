package uz.stajirovka.jbooking.service.impl;

import org.springframework.stereotype.Service;
import uz.stajirovka.jbooking.entity.RoomEntity;
import uz.stajirovka.jbooking.service.BookingPriceService;

import java.math.BigDecimal;

@Service
public class BookingPriceServiceImpl implements BookingPriceService {

    @Override
    public BigDecimal calculate(RoomEntity room, long nights) {
        return room.getPricePerNight().multiply(BigDecimal.valueOf(nights));
    }
}
