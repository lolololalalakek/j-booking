package uz.stajirovka.jbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.stajirovka.jbooking.entity.PaymentTransactionEntity;

import java.util.List;
import java.util.Optional;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransactionEntity, Long> {

    Optional<PaymentTransactionEntity> findByTransactionId(Long transactionId);

    List<PaymentTransactionEntity> findByBookingId(Long bookingId);
}
