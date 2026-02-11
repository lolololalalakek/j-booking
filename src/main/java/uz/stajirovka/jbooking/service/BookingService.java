package uz.stajirovka.jbooking.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import uz.stajirovka.jbooking.dto.request.BookingConfirmRequest;
import uz.stajirovka.jbooking.dto.request.BookingCreateRequest;
import uz.stajirovka.jbooking.dto.response.BookingConfirmResponse;
import uz.stajirovka.jbooking.dto.response.BookingResponse;


public interface BookingService {

    // создание нового бронирования
    BookingResponse initBooking(BookingCreateRequest request);

    BookingConfirmResponse confirmBooking(BookingConfirmRequest request);

    // получение бронирования по идентификатору
    BookingResponse getById(Long id);

    // получение всех бронирований с пагинацией
    Slice<BookingResponse> getAll(Pageable pageable);

    // получение истории бронирований по идентификатору гостя
    Slice<BookingResponse> getByGuestId(Long guestId, Pageable pageable);
}
