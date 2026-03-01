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

public final class HotelSpecifications {

    private HotelSpecifications() {
    }

    public static Specification<HotelEntity> search(LocalDateTime checkInDate,
                                                    LocalDateTime checkOutDate,
                                                    Integer guests,
                                                    Long cityId,
                                                    Long minPrice,
                                                    Long maxPrice,
                                                    Integer minStars,
                                                    Set<Amenity> amenities) {
        return (root, query, cb) -> {
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isNull(root.get("deletedAt")));

            if (cityId != null) {
                predicates.add(cb.equal(root.get("city").get("id"), cityId));
            }
            if (minStars != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("stars"), minStars));
            }

            Subquery<Long> roomSubquery = query.subquery(Long.class);
            var roomRoot = roomSubquery.from(RoomEntity.class);
            List<Predicate> roomPredicates = new ArrayList<>();
            roomPredicates.add(cb.equal(roomRoot.get("hotel").get("id"), root.get("id")));
            roomPredicates.add(cb.isNull(roomRoot.get("deletedAt")));
            roomPredicates.add(cb.greaterThanOrEqualTo(roomRoot.get("capacity"), guests));

            if (minPrice != null) {
                roomPredicates.add(cb.greaterThanOrEqualTo(roomRoot.get("pricePerNight"), minPrice));
            }
            if (maxPrice != null) {
                roomPredicates.add(cb.lessThanOrEqualTo(roomRoot.get("pricePerNight"), maxPrice));
            }
            if (amenities != null && !amenities.isEmpty()) {
                Subquery<Long> amenitiesCountSubquery = roomSubquery.subquery(Long.class);
                var amenitiesRoomRoot = amenitiesCountSubquery.from(RoomEntity.class);
                var amenitiesJoin = amenitiesRoomRoot.joinSet("amenities");
                amenitiesCountSubquery.select(cb.countDistinct(amenitiesJoin));
                amenitiesCountSubquery.where(
                    cb.equal(amenitiesRoomRoot.get("id"), roomRoot.get("id")),
                    amenitiesJoin.in(amenities)
                );
                roomPredicates.add(cb.equal(amenitiesCountSubquery, (long) amenities.size()));
            }

            Subquery<Long> bookingSubquery = roomSubquery.subquery(Long.class);
            var bookingRoot = bookingSubquery.from(BookingEntity.class);
            bookingSubquery.select(bookingRoot.get("id"));
            bookingSubquery.where(
                cb.equal(bookingRoot.get("room").get("id"), roomRoot.get("id")),
                cb.notEqual(bookingRoot.get("status"), BookingStatus.CANCELLED),
                cb.isNull(bookingRoot.get("deletedAt")),
                cb.lessThan(bookingRoot.get("checkInDate"), checkOutDate),
                cb.greaterThan(bookingRoot.get("checkOutDate"), checkInDate)
            );
            roomPredicates.add(cb.not(cb.exists(bookingSubquery)));

            roomSubquery.select(roomRoot.get("id"));
            roomSubquery.where(roomPredicates.toArray(new Predicate[0]));
            predicates.add(cb.exists(roomSubquery));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
