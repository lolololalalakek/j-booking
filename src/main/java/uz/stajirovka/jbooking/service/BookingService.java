package uz.stajirovka.jbooking.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import uz.stajirovka.jbooking.dto.request.BookingCreateRequest;
import uz.stajirovka.jbooking.dto.response.BookingResponse;

public interface BookingService {

    BookingResponse create(BookingCreateRequest request);

    BookingResponse getById(Long id);

    Slice<BookingResponse> getAll(Pageable pageable);

    BookingResponse cancel(Long id);

    BookingResponse confirm(Long id);

    void delete(Long id);
}
