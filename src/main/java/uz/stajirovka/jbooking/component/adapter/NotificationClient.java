package uz.stajirovka.jbooking.component.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.stajirovka.jbooking.configuration.NotifciationFeignConfiguration;
import uz.stajirovka.jbooking.dto.request.NotificationRequest;
import uz.stajirovka.jbooking.dto.response.CommonResponse;
import uz.stajirovka.jbooking.dto.response.NotificationResponse;

@FeignClient(
    name = "notificationClient",
    url = "${client.notification.url}",
    configuration = NotifciationFeignConfiguration.class
)
public interface NotificationClient {

    @PostMapping("/api/notifications/sending")
    // retrayable позволяет повторно отправлять запросы
    @Retryable(delay = 500L) // delay - периодичность повторов. по дефолту 1000
    CommonResponse<NotificationResponse> sendNotification(@RequestBody NotificationRequest request);



}
