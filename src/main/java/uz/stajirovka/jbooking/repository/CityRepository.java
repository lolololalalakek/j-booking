package uz.stajirovka.jbooking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.stajirovka.jbooking.entity.CityEntity;

public interface CityRepository extends JpaRepository<CityEntity, Long> {

    Slice<CityEntity> findAllBy(Pageable pageable);
}
