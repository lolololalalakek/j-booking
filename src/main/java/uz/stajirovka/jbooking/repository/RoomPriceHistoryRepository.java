package uz.stajirovka.jbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.stajirovka.jbooking.entity.RoomPriceHistoryEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RoomPriceHistoryRepository extends JpaRepository<RoomPriceHistoryEntity, Long> {

    // Получить актуальную запись цены (valid_to IS NULL)
    Optional<RoomPriceHistoryEntity> findByRoomIdAndValidToIsNull(Long roomId);

    // Получить всю историю цен для комнаты (отсортировано по дате)
    List<RoomPriceHistoryEntity> findByRoomIdOrderByValidFromDesc(Long roomId);

    // Закрыть текущую запись цены
    @Modifying
    @Query("UPDATE RoomPriceHistoryEntity h SET h.validTo = :validTo WHERE h.room.id = :roomId AND h.validTo IS NULL")
    void closeCurrentPrice(@Param("roomId") Long roomId, @Param("validTo") LocalDateTime validTo);

    // Получить цену на конкретную дату (для отчётов)
    @Query("""
        SELECT h.pricePerNight FROM RoomPriceHistoryEntity h
        WHERE h.room.id = :roomId
          AND h.validFrom <= :date
          AND (h.validTo IS NULL OR h.validTo > :date)
        """)
    Optional<BigDecimal> findPriceAtDate(@Param("roomId") Long roomId, @Param("date") LocalDateTime date);
}
