package uz.stajirovka.jbooking.dto.request;

import uz.stajirovka.jbooking.constant.enums.NotificationType;

public record NotificationRequest(
    ReceiverDto receiver,
    NotificationType type,
    String text

) {
}
