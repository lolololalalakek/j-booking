package uz.stajirovka.jbooking.repository.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import uz.stajirovka.jbooking.constant.enums.Amenity;
import uz.stajirovka.jbooking.constant.enums.BookingStatus;
import uz.stajirovka.jbooking.entity.BookingEntity;
import uz.stajirovka.jbooking.entity.RoomEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

// утилитный класс с JPA-спецификациями для поиска доступных номеров конкретного отеля
public final class RoomSpecifications {

    // запрещаю создавать экземпляры — класс используется только через статические методы
    private RoomSpecifications() {
    }

    // ищу свободные номера конкретного отеля на указанные даты с учётом фильтров
    public static Specification<RoomEntity> availableForHotel(Long hotelId,
                                                              LocalDateTime checkInDate,
                                                              LocalDateTime checkOutDate,
                                                              Integer guests,
                                                              Long minPrice,
                                                              Long maxPrice,
                                                              Set<Amenity> amenities) {
        return (root, query, cb) -> {
            // убираю дубликаты — номер может задвоиться из-за join по удобствам
            query.distinct(true);

            // список условий, которые будут объединены через and
            ArrayList<Predicate> predicates = new ArrayList<>();

            // номер должен принадлежать запрошенному отелю
            predicates.add(cb.equal(root.get("hotel").get("id"), hotelId));

            // показываю только не удалённые номера (мягкое удаление через deletedAt)
            predicates.add(cb.isNull(root.get("deletedAt")));

            // вместимость номера должна быть не меньше количества гостей
            predicates.add(cb.greaterThanOrEqualTo(root.get("capacity"), guests));

            // если задана минимальная цена — фильтрую по ней
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("pricePerNight"), minPrice));
            }

            // если задана максимальная цена — фильтрую по ней
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("pricePerNight"), maxPrice));
            }

            // подзапрос: проверяю что на номер нет активного бронирования на указанные даты
            var bookingSubquery = query.subquery(Long.class);
            var bookingRoot = bookingSubquery.from(BookingEntity.class);
            bookingSubquery.select(bookingRoot.get("id"));
            bookingSubquery.where(
                // бронирование должно быть именно на этот номер
                cb.equal(bookingRoot.get("room").get("id"), root.get("id")),
                // игнорирую отменённые бронирования — они не блокируют номер
                cb.notEqual(bookingRoot.get("status"), BookingStatus.CANCELLED),
                // бронирование не должно быть удалено
                cb.isNull(bookingRoot.get("deletedAt")),
                // проверяю пересечение дат: бронирование начинается до нашего выезда
                cb.lessThan(bookingRoot.get("checkInDate"), checkOutDate),
                // и заканчивается после нашего заезда — значит даты пересекаются
                cb.greaterThan(bookingRoot.get("checkOutDate"), checkInDate)
            );

            // номер подходит только если на него нет пересекающихся активных бронирований
            predicates.add(cb.not(cb.exists(bookingSubquery)));

            // если переданы удобства — проверяю что номер содержит все из них
            if (amenities != null && !amenities.isEmpty()) {
                // подзапрос считает количество совпадающих удобств у конкретного номера
                var amenitiesCountSubquery = query.subquery(Long.class);
                var amenitiesRoomRoot = amenitiesCountSubquery.from(RoomEntity.class);
                var amenitiesJoin = amenitiesRoomRoot.joinSet("amenities");
                amenitiesCountSubquery.select(cb.countDistinct(amenitiesJoin));
                amenitiesCountSubquery.where(
                    cb.equal(amenitiesRoomRoot.get("id"), root.get("id")),
                    // считаю только те удобства, которые есть в запрошенном списке
                    amenitiesJoin.in(amenities)
                );
                // требую чтобы количество совпавших удобств равнялось количеству запрошенных — т.е. все должны быть
                predicates.add(cb.equal(amenitiesCountSubquery, (long) amenities.size()));
            }

            // объединяю все условия через and и возвращаю итоговый предикат
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
