package uz.stajirovka.jbooking.component.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.stajirovka.jbooking.configuration.NotifciationFeignConfiguration;
import uz.stajirovka.jbooking.dto.request.NotificationRequest;
import uz.stajirovka.jbooking.dto.response.CommonResponse;
import uz.stajirovka.jbooking.dto.response.NotificationResponse;

@FeignClient(
    name = "notificationClient",
    url = "${spring.cloud.openfeign.client.config.notificationClient.url}",
    configuration = NotifciationFeignConfiguration.class
)
public interface NotificationClient {

    @PostMapping("/api/notifications/sending")
    CommonResponse<NotificationResponse> sendNotification(@RequestBody NotificationRequest request);


}
