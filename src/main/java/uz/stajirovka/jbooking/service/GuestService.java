package uz.stajirovka.jbooking.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import uz.stajirovka.jbooking.dto.request.GuestCreateRequest;
import uz.stajirovka.jbooking.dto.response.BookingResponse;
import uz.stajirovka.jbooking.dto.response.GuestResponse;

public interface GuestService {

    // создание нового гостя
    GuestResponse create(GuestCreateRequest request);

    // получение гостя по идентификатору
    GuestResponse getById(Long id);

    // получение всех гостей с пагинацией
    Slice<GuestResponse> getAll(Pageable pageable);

    // получение гостя по email
    GuestResponse getByEmail(String email);

    // обновление данных гостя
    GuestResponse update(Long id, GuestCreateRequest request);

    // удаление гостя
    void delete(Long id);

    // получение бронирований гостя по email
    Slice<BookingResponse> getBookingsByEmail(String email, Pageable pageable);
}
