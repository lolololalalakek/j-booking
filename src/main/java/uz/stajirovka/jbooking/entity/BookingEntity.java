package uz.stajirovka.jbooking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import uz.stajirovka.jbooking.constant.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    RoomEntity room;

    @Column(name = "guest_first_name", nullable = false)
    String guestFirstName;

    @Column(name = "guest_last_name", nullable = false)
    String guestLastName;

    @Column(name = "guest_email")
    String guestEmail;

    @Column(name = "number_of_guests", nullable = false)
    Integer numberOfGuests;

    @Column(name = "check_in_date", nullable = false)
    LocalDateTime checkInDate;

    @Column(name = "check_out_date", nullable = false)
    LocalDateTime checkOutDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    BookingStatus status;

    @Column(name = "total_price")
    BigDecimal totalPrice;

    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    LocalDateTime updatedAt;
}
