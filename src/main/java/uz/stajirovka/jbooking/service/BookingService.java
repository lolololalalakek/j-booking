package uz.stajirovka.jbooking.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import uz.stajirovka.jbooking.constant.enums.Currency;
import uz.stajirovka.jbooking.dto.request.BookingConfirmRequest;
import uz.stajirovka.jbooking.dto.request.BookingCreateRequest;
import uz.stajirovka.jbooking.dto.response.BookingConfirmResponse;
import uz.stajirovka.jbooking.dto.response.BookingResponse;

public interface BookingService {

    BookingResponse initBooking(BookingCreateRequest request);

    BookingConfirmResponse confirmBooking(BookingConfirmRequest request);

    BookingResponse getById(Long id, Currency currency);

    Slice<BookingResponse> getAllByPinfl(String pinfl, Currency currency, Pageable pageable);
}
