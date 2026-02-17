package uz.stajirovka.jbooking.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.stajirovka.jbooking.entity.GuestEntity;

import java.util.Optional;

public interface GuestRepository extends JpaRepository<GuestEntity, Long> {

    // Pessimistic lock для предотвращения race condition при создании гостя
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT g FROM GuestEntity g WHERE g.pinfl = :pinfl")
    Optional<GuestEntity> findByPinflWithLock(@Param("pinfl") String pinfl);
}
