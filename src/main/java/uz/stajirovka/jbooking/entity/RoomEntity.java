package uz.stajirovka.jbooking.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.BatchSize;
import uz.stajirovka.jbooking.constant.enums.Amenity;
import uz.stajirovka.jbooking.constant.enums.MealPlan;
import uz.stajirovka.jbooking.constant.enums.RoomType;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "rooms")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "room_number", nullable = false)
    String roomNumber;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "room_type", nullable = false)
    RoomType roomType;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "meal_plan", nullable = false)
    MealPlan mealPlan;

    @ElementCollection(targetClass = Amenity.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "room_amenities", joinColumns = @JoinColumn(name = "room_id"))
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "amenity")
    @BatchSize(size = 20)
    Set<Amenity> amenities;

    @Column(nullable = false)
    Integer capacity;

    @Column(name = "price_per_night", nullable = false)
    Long pricePerNight;

    @Column(length = 500)
    String description;

    @Column(nullable = false)
    LocalDateTime createdAt;

    @Column(nullable = false)
    LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    LocalDateTime deletedAt;
}
