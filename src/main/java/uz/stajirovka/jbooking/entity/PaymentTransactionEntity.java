package uz.stajirovka.jbooking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "transaction_id", nullable = false, unique = true)
    Long transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    BookingEntity booking;

    @Column(name = "reference_id", nullable = false)
    Long referenceId;

    @Column(nullable = false, length = 50)
    String status;

    @Column(nullable = false)
    Long amount;

    @Column(nullable = false, length = 10)
    String currency;

    @Column(name = "transaction_created_at")
    LocalDateTime transactionCreatedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;
}
