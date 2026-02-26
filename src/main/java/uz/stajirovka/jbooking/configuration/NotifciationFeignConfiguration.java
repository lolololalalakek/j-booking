package uz.stajirovka.jbooking.configuration;


import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import uz.stajirovka.jbooking.component.properties.NotificationProperties;

import java.util.Base64;

@RequiredArgsConstructor
public class NotifciationFeignConfiguration {

    private final NotificationProperties notificationProperties;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> requestTemplate.header(HttpHeaders.AUTHORIZATION, encode());
    }

    private String encode() {
        String authToken = String.format("%s:%s", notificationProperties.getLogin(), notificationProperties.getPassword());
        return Base64.getEncoder().encodeToString(authToken.getBytes());
    }


}
