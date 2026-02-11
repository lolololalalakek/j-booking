package uz.stajirovka.jbooking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import uz.stajirovka.jbooking.dto.request.PaymentRequest;
import uz.stajirovka.jbooking.dto.response.PaymentResponse;

@FeignClient(
    name = "paymentClient",
    url =  "${client.payment.url}"
)
public interface PaymentClient {


    @PostMapping("/pay")
    PaymentResponse executePayment (PaymentRequest request);
}
