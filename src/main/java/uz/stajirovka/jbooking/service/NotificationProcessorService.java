package uz.stajirovka.jbooking.service;

import uz.stajirovka.jbooking.entity.BookingEntity;

public interface NotificationProcessorService {
    void process(BookingEntity entity);
}
