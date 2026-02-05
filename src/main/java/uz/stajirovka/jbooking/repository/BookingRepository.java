package uz.stajirovka.jbooking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.stajirovka.jbooking.entity.BookingEntity;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    Slice<BookingEntity> findAllBy(Pageable pageable);
}
