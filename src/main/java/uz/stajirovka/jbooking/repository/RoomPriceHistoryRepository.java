package uz.stajirovka.jbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.stajirovka.jbooking.entity.RoomPriceHistoryEntity;

import java.time.LocalDateTime;

public interface RoomPriceHistoryRepository extends JpaRepository<RoomPriceHistoryEntity, Long> {

    // Закрыть текущую запись цены
    @Modifying
    @Query("UPDATE RoomPriceHistoryEntity h SET h.validTo = :validTo WHERE h.room.id = :roomId AND h.validTo IS NULL")
    void closeCurrentPrice(@Param("roomId") Long roomId, @Param("validTo") LocalDateTime validTo);
}
