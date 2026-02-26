package uz.stajirovka.jbooking.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "J-Booking API",
        version = "1.0",
        description = "Hotel booking service",
        contact = @Contact(name = "stajirovka")
    ),
    servers = @Server(url = "http://localhost:8080", description = "Local")
)
public class OpenApiConfig {
}
