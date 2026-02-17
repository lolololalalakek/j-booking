package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.dto.request.GuestInfoRequest;
import uz.stajirovka.jbooking.entity.GuestEntity;
import uz.stajirovka.jbooking.repository.GuestRepository;
import uz.stajirovka.jbooking.service.GuestService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;

    // поиск гостя по ПИНФЛ или создание нового
    @Override
    @Transactional
    public GuestEntity findOrCreateGuest(GuestInfoRequest guestInfo) {
        return guestRepository.findByPinflWithLock(guestInfo.pinfl())
                .orElseGet(() -> createGuestEntity(guestInfo));
    }

    private GuestEntity createGuestEntity(GuestInfoRequest guestInfo) {
        GuestEntity guest = new GuestEntity();
        guest.setFirstName(guestInfo.firstName());
        guest.setLastName(guestInfo.lastName());
        guest.setPinfl(guestInfo.pinfl());
        guest.setEmail(guestInfo.email());
        guest.setPhone(guestInfo.phone());
        guest.setCreatedAt(LocalDateTime.now());
        return guestRepository.save(guest);
    }
}
