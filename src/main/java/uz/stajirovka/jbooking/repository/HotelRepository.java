package uz.stajirovka.jbooking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.stajirovka.jbooking.entity.HotelEntity;

public interface HotelRepository extends JpaRepository<HotelEntity, Long>, JpaSpecificationExecutor<HotelEntity> {

    @Query("SELECT h FROM CityEntity c JOIN c.hotels h WHERE c.id = :cityId AND h.deletedAt IS NULL")
    Slice<HotelEntity> findByCityId(@Param("cityId") Long cityId, Pageable pageable);

}
