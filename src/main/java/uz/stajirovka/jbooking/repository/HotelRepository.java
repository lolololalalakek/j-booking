package uz.stajirovka.jbooking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.stajirovka.jbooking.entity.HotelEntity;

public interface HotelRepository extends JpaRepository<HotelEntity, Long> {

    Slice<HotelEntity> findAllBy(Pageable pageable);

    Slice<HotelEntity> findByCityId(Long cityId, Pageable pageable);
}
