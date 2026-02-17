package uz.stajirovka.jbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ConfigurationPropertiesScan("uz.stajirovka.jbooking.component.properties")
@EnableFeignClients(
    basePackages = "uz.stajirovka.jbooking.component.adapter"
)
@EnableScheduling
public class JBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(JBookingApplication.class, args);
    }

}
