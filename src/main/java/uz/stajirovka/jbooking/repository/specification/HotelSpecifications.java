package uz.stajirovka.jbooking.repository.specification;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import uz.stajirovka.jbooking.constant.enums.Amenity;
import uz.stajirovka.jbooking.constant.enums.BookingStatus;
import uz.stajirovka.jbooking.entity.BookingEntity;
import uz.stajirovka.jbooking.entity.HotelEntity;
import uz.stajirovka.jbooking.entity.RoomEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// утилитный класс с JPA-спецификациями для поиска отелей
public final class HotelSpecifications {

    // запрещаю создавать экземпляры — класс используется только через статические методы
    private HotelSpecifications() {
    }

    // динамический поиск отелей по набору фильтров: даты, гости, город, цена, звёзды, удобства
    public static Specification<HotelEntity> search(LocalDateTime checkInDate,
                                                    LocalDateTime checkOutDate,
                                                    Integer guests,
                                                    Long cityId,
                                                    Long minPrice,
                                                    Long maxPrice,
                                                    Integer minStars,
                                                    Set<Amenity> amenities) {
        return (root, query, cb) -> {
            // убираю дубликаты — отель может попасть в результат несколько раз через join-ы
            query.distinct(true);

            // список условий, которые будут объединены через and
            List<Predicate> predicates = new ArrayList<>();

            // показываю только не удалённые отели (мягкое удаление через deletedAt)
            predicates.add(cb.isNull(root.get("deletedAt")));

            // если передан город — фильтрую по нему
            predicates.add(cb.equal(root.get("city").get("id"), cityId));

            // если задан минимальный рейтинг звёзд — фильтрую по нему
            if (minStars != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("stars"), minStars));
            }

            // подзапрос: ищу среди номеров отеля хотя бы один подходящий
            Subquery<Long> roomSubquery = query.subquery(Long.class);
            var roomRoot = roomSubquery.from(RoomEntity.class);
            List<Predicate> roomPredicates = new ArrayList<>();

            // номер должен принадлежать текущему отелю
            roomPredicates.add(cb.equal(roomRoot.get("hotel").get("id"), root.get("id")));

            // номер не должен быть удалён
            roomPredicates.add(cb.isNull(roomRoot.get("deletedAt")));

            // вместимость номера должна быть не меньше количества гостей
            roomPredicates.add(cb.greaterThanOrEqualTo(roomRoot.get("capacity"), guests));

            // если задана минимальная цена — фильтрую по ней
            if (minPrice != null) {
                roomPredicates.add(cb.greaterThanOrEqualTo(roomRoot.get("pricePerNight"), minPrice));
            }

            // если задана максимальная цена — фильтрую по ней
            if (maxPrice != null) {
                roomPredicates.add(cb.lessThanOrEqualTo(roomRoot.get("pricePerNight"), maxPrice));
            }

            // если переданы удобства — проверяю что номер содержит все из них
            if (amenities != null && !amenities.isEmpty()) {
                // подзапрос считает количество совпадающих удобств у конкретного номера
                Subquery<Long> amenitiesCountSubquery = roomSubquery.subquery(Long.class);
                var amenitiesRoomRoot = amenitiesCountSubquery.from(RoomEntity.class);
                var amenitiesJoin = amenitiesRoomRoot.joinSet("amenities");
                amenitiesCountSubquery.select(cb.countDistinct(amenitiesJoin));
                amenitiesCountSubquery.where(
                    cb.equal(amenitiesRoomRoot.get("id"), roomRoot.get("id")),
                    // считаю только те удобства, которые есть в запрошенном списке
                    amenitiesJoin.in(amenities)
                );
                // требую чтобы количество совпавших удобств равнялось количеству запрошенных — т.е. все должны быть
                roomPredicates.add(cb.equal(amenitiesCountSubquery, (long) amenities.size()));
            }

            // подзапрос: проверяю что на номер нет активного бронирования на указанные даты
            Subquery<Long> bookingSubquery = roomSubquery.subquery(Long.class);
            var bookingRoot = bookingSubquery.from(BookingEntity.class);
            bookingSubquery.select(bookingRoot.get("id"));
            bookingSubquery.where(
                // бронирование должно быть на этот номер
                cb.equal(bookingRoot.get("room").get("id"), roomRoot.get("id")),
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
            roomPredicates.add(cb.not(cb.exists(bookingSubquery)));

            // применяю все условия к подзапросу по номерам и возвращаю id номера
            roomSubquery.select(roomRoot.get("id"));
            roomSubquery.where(roomPredicates.toArray(new Predicate[0]));

            // отель попадает в результат только если у него есть хотя бы один подходящий номер
            predicates.add(cb.exists(roomSubquery));

            // объединяю все условия через and и возвращаю итоговый предикат
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
