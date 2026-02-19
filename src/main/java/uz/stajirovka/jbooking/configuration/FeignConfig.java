package uz.stajirovka.jbooking.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(
    basePackages = "uz.stajirovka.jbooking.configuration"
)
@Configuration
public class FeignConfig {




}
