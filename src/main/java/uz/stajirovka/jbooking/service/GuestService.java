package uz.stajirovka.jbooking.service;

import uz.stajirovka.jbooking.dto.request.GuestInfoRequest;
import uz.stajirovka.jbooking.entity.GuestEntity;

public interface GuestService {

    // поиск гостя по email или создание нового
    GuestEntity findOrCreateGuest(GuestInfoRequest guestInfo);
}
