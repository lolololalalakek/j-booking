package uz.stajirovka.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.stajirovka.jbooking.dto.request.BookingPaymentRequest;
import uz.stajirovka.jbooking.dto.request.PaymentRequest;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = {UUID.class, java.util.Random.class})
public interface PaymentMapper {

    @Mapping(target = "referenceId", expression = "java(UUID.randomUUID())")
    @Mapping(target = "type", constant = "PAYMENT")
    @Mapping(target = "receiverName", constant = "example")
    @Mapping(target = "receiverToken", expression = "java(UUID.randomUUID().toString())")
    @Mapping(target = "merchantId", expression = "java(Math.abs(new Random().nextLong()) + 1)")
    PaymentRequest map(BookingPaymentRequest request);

}
