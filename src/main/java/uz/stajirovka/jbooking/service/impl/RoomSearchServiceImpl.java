package uz.stajirovka.jbooking.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.constant.enums.Amenity;
import uz.stajirovka.jbooking.constant.enums.BookingStatus;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.dto.request.RoomSearchRequest;
import uz.stajirovka.jbooking.exception.ValidationException;
import uz.stajirovka.jbooking.dto.response.RoomResponse;
import uz.stajirovka.jbooking.entity.BookingEntity;
import uz.stajirovka.jbooking.entity.RoomEntity;
import uz.stajirovka.jbooking.mapper.RoomMapper;
import uz.stajirovka.jbooking.service.RoomSearchService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomSearchServiceImpl implements RoomSearchService {

    private final EntityManager entityManager;
    private final RoomMapper roomMapper;

    @Override
    public Slice<RoomResponse> search(RoomSearchRequest request, Pageable pageable) {
        // валидация дат
        if (!request.checkOutDate().isAfter(request.checkInDate())) {
            throw new ValidationException(Error.VALIDATION_ERROR, "Дата выезда должна быть после даты заезда");
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RoomEntity> query = cb.createQuery(RoomEntity.class);
        Root<RoomEntity> room = query.from(RoomEntity.class);

        // fetch join для hotel
        room.fetch("hotel");

        List<Predicate> predicates = new ArrayList<>();

        // вместимость >= количество гостей
        predicates.add(cb.greaterThanOrEqualTo(room.get("capacity"), request.guests()));

        // фильтр по городу
        if (request.cityId() != null) {
            predicates.add(cb.equal(room.get("hotel").get("city").get("id"), request.cityId()));
        }

        // фильтр по отелю
        if (request.hotelId() != null) {
            predicates.add(cb.equal(room.get("hotel").get("id"), request.hotelId()));
        }

        // фильтр по минимальной цене
        if (request.minPrice() != null) {
            predicates.add(cb.greaterThanOrEqualTo(room.get("pricePerNight"), request.minPrice()));
        }

        // фильтр по максимальной цене
        if (request.maxPrice() != null) {
            predicates.add(cb.lessThanOrEqualTo(room.get("pricePerNight"), request.maxPrice()));
        }

        // фильтр по удобствам (все указанные должны присутствовать)
        if (request.amenities() != null && !request.amenities().isEmpty()) {
            for (Amenity amenity : request.amenities()) {
                predicates.add(cb.isMember(amenity, room.get("amenities")));
            }
        }

        // исключаем номера с пересекающимися бронированиями
        Subquery<Long> bookingSubquery = query.subquery(Long.class);
        Root<BookingEntity> booking = bookingSubquery.from(BookingEntity.class);
        bookingSubquery.select(booking.get("room").get("id"))
                .where(
                        cb.equal(booking.get("room").get("id"), room.get("id")),
                        cb.notEqual(booking.get("status"), BookingStatus.CANCELLED),
                        cb.isNull(booking.get("deletedAt")),
                        cb.lessThan(booking.get("checkInDate"), request.checkOutDate()),
                        cb.greaterThan(booking.get("checkOutDate"), request.checkInDate())
                );

        predicates.add(cb.not(cb.exists(bookingSubquery)));

        // не удалённые комнаты
        predicates.add(cb.isNull(room.get("deletedAt")));

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.asc(room.get("pricePerNight")));

        TypedQuery<RoomEntity> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize() + 1);

        List<RoomEntity> results = typedQuery.getResultList();

        boolean hasNext = results.size() > pageable.getPageSize();
        if (hasNext) {
            results = results.subList(0, pageable.getPageSize());
        }

        List<RoomResponse> responses = results.stream()
                .map(roomMapper::toResponse)
                .toList();

        return new SliceImpl<>(responses, pageable, hasNext);
    }
}
