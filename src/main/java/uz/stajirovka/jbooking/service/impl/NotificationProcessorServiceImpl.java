package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.component.adapter.NotificationClient;
import uz.stajirovka.jbooking.repository.BookingRepository;
import uz.stajirovka.jbooking.constant.enums.NotificationType;
import uz.stajirovka.jbooking.dto.request.NotificationRequest;
import uz.stajirovka.jbooking.dto.request.ReceiverDto;
import uz.stajirovka.jbooking.dto.response.NotificationResponse;
import uz.stajirovka.jbooking.entity.BookingEntity;
import uz.stajirovka.jbooking.entity.GuestEntity;
import uz.stajirovka.jbooking.service.NotificationProcessorService;


@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationProcessorServiceImpl implements NotificationProcessorService {

    private final NotificationClient notificationClient;
    private final BookingRepository bookingRepository;
    private static final String MESSAGE = "БРОНИРОВАНИЕ ПОДТВЕРЖДЕНО, НОМЕР БРОНИРОВАНИЯ: %s";

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void process(BookingEntity entity) {
        GuestEntity mainGuest = entity.getMainGuest();
        ReceiverDto receiverDto = new ReceiverDto(null, mainGuest.getEmail(), null);
        NotificationRequest request = new NotificationRequest(
            receiverDto, NotificationType.EMAIL, String.format(MESSAGE, entity.getId())
        );
        try {
            NotificationResponse notificationResponse = notificationClient
                .sendNotification(request)
                .data();
            entity.setNotificationId(notificationResponse.notificationId());
            bookingRepository.save(entity);
        } catch (Exception e) {
            log.error("Error while sending notification: {}", e.getMessage(), e);
        }
    }


}
