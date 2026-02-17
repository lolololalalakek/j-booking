package uz.stajirovka.jbooking.component.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.stajirovka.jbooking.dto.request.PaymentRequest;
import uz.stajirovka.jbooking.dto.response.PaymentResponse;

@FeignClient(
    name = "paymentClient",
    url =  "${client.payment.url}"
)
public interface PaymentClient {

    @PostMapping("/api/v1/transactions")
    PaymentResponse executePayment(@RequestBody PaymentRequest request);
}
