package uz.stajirovka.jbooking.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import uz.stajirovka.jbooking.constant.enums.Currency;
import uz.stajirovka.jbooking.dto.request.RoomSearchRequest;
import uz.stajirovka.jbooking.dto.response.RoomResponse;

// сервис поиска доступных номеров
public interface RoomSearchService {

    // поиск доступных номеров по критериям
    Slice<RoomResponse> search(RoomSearchRequest request, Currency currency, Pageable pageable);
}
