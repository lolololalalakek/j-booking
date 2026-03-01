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

public final class RoomSpecifications {

    private RoomSpecifications() {
    }

    public static Specification<RoomEntity> availableForHotel(Long hotelId,
                                                              LocalDateTime checkInDate,
                                                              LocalDateTime checkOutDate,
                                                              Integer guests,
                                                              Long minPrice,
                                                              Long maxPrice,
                                                              Set<Amenity> amenities) {
        return (root, query, cb) -> {
            query.distinct(true);

            ArrayList<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("hotel").get("id"), hotelId));
            predicates.add(cb.isNull(root.get("deletedAt")));
            predicates.add(cb.greaterThanOrEqualTo(root.get("capacity"), guests));

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("pricePerNight"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("pricePerNight"), maxPrice));
            }

            var bookingSubquery = query.subquery(Long.class);
            var bookingRoot = bookingSubquery.from(BookingEntity.class);
            bookingSubquery.select(bookingRoot.get("id"));
            bookingSubquery.where(
                cb.equal(bookingRoot.get("room").get("id"), root.get("id")),
                cb.notEqual(bookingRoot.get("status"), BookingStatus.CANCELLED),
                cb.isNull(bookingRoot.get("deletedAt")),
                cb.lessThan(bookingRoot.get("checkInDate"), checkOutDate),
                cb.greaterThan(bookingRoot.get("checkOutDate"), checkInDate)
            );
            predicates.add(cb.not(cb.exists(bookingSubquery)));

            if (amenities != null && !amenities.isEmpty()) {
                var amenitiesCountSubquery = query.subquery(Long.class);
                var amenitiesRoomRoot = amenitiesCountSubquery.from(RoomEntity.class);
                var amenitiesJoin = amenitiesRoomRoot.joinSet("amenities");
                amenitiesCountSubquery.select(cb.countDistinct(amenitiesJoin));
                amenitiesCountSubquery.where(
                    cb.equal(amenitiesRoomRoot.get("id"), root.get("id")),
                    amenitiesJoin.in(amenities)
                );
                predicates.add(cb.equal(amenitiesCountSubquery, (long) amenities.size()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
