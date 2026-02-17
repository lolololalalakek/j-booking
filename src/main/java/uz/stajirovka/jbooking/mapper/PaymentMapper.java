package uz.stajirovka.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.stajirovka.jbooking.dto.request.BookingConfirmRequest;
import uz.stajirovka.jbooking.dto.request.PaymentRequest;
import uz.stajirovka.jbooking.entity.BookingEntity;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "bookingEntity.id", target = "referenceId")
    @Mapping(target = "type", constant = "PAYMENT")
    PaymentRequest map(BookingConfirmRequest request, BookingEntity bookingEntity);

}
