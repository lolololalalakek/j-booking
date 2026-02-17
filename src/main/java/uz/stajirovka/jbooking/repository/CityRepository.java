package uz.stajirovka.jbooking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.stajirovka.jbooking.entity.CityEntity;

import java.util.Optional;

public interface CityRepository extends JpaRepository<CityEntity, Long> {

    Slice<CityEntity> findAllBy(Pageable pageable);

    @Query("SELECT c FROM CityEntity c JOIN c.hotels h WHERE h.id = :hotelId")
    Optional<CityEntity> findByHotelId(@Param("hotelId") Long hotelId);
}
