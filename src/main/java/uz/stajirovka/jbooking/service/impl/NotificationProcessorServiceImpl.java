package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.stajirovka.jbooking.component.adapter.NotificationClient;
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
    private static final String MESSAGE = "БРОНИРОВАНИЕ ПОДТВЕРЖДЕНО, НОМЕР БРОНИРОВАНИЯ: %s";

    @Override
    public void process(BookingEntity entity) {
        try {
            GuestEntity mainGuest = entity.getMainGuest();
            ReceiverDto receiverDto = new ReceiverDto(null, mainGuest.getEmail(), null);
            NotificationRequest request = new NotificationRequest(
                receiverDto, NotificationType.EMAIL, String.format(MESSAGE, entity.getId())
            );
            NotificationResponse notificationResponse = notificationClient
                .sendNotification(request)
                .data();
            log.info("Notification response from email: {}", notificationResponse);
            entity.setNotificationId(notificationResponse.notificationId());
        } catch (Exception e) {
            log.error("Error while sending notification: {}", e.getMessage(), e);
        }
    }


}
