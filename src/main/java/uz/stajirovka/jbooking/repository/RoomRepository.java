package uz.stajirovka.jbooking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.stajirovka.jbooking.entity.RoomEntity;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    Slice<RoomEntity> findAllBy(Pageable pageable);

    Slice<RoomEntity> findByHotelId(Long hotelId, Pageable pageable);
}
