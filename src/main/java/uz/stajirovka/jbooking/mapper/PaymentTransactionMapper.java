package uz.stajirovka.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.stajirovka.jbooking.dto.response.PaymentResponse;
import uz.stajirovka.jbooking.entity.BookingEntity;
import uz.stajirovka.jbooking.entity.PaymentTransactionEntity;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface PaymentTransactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "paymentResponse.id", target = "transactionId")
    @Mapping(source = "booking", target = "booking")
    @Mapping(source = "paymentResponse.referenceId", target = "referenceId")
    @Mapping(source = "paymentResponse.status", target = "status")
    @Mapping(source = "paymentResponse.amount", target = "amount")
    @Mapping(expression = "java(paymentResponse.currency().name())", target = "currency")
    @Mapping(source = "paymentResponse.createdAt", target = "transactionCreatedAt")
    @Mapping(source = "createdAt", target = "createdAt")
    PaymentTransactionEntity toEntity(PaymentResponse paymentResponse, BookingEntity booking, LocalDateTime createdAt);
}