package uz.stajirovka.jbooking.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.stajirovka.jbooking.component.properties.BookingProperties;
import uz.stajirovka.jbooking.constant.enums.BookingStatus;
import uz.stajirovka.jbooking.dto.request.BookingConfirmRequest;
import uz.stajirovka.jbooking.dto.request.BookingCreateRequest;
import uz.stajirovka.jbooking.dto.request.GuestInfoRequest;
import uz.stajirovka.jbooking.entity.BookingEntity;
import uz.stajirovka.jbooking.entity.RoomEntity;
import uz.stajirovka.jbooking.exception.ConflictException;
import uz.stajirovka.jbooking.exception.NotFoundException;
import uz.stajirovka.jbooking.exception.ValidationException;
import uz.stajirovka.jbooking.repository.BookingRepository;
import uz.stajirovka.jbooking.repository.RoomRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock private BookingRepository bookingRepository;
    @Mock private RoomRepository roomRepository;
    @Mock private BookingProperties bookingProperties;

    @InjectMocks
    private BookingServiceImpl bookingService;

    // номер занят на выбранные даты — должен выброситься ConflictException
    @Test
    void initBooking_whenRoomIsNotAvailable_throwsConflictException() {
        LocalDateTime checkIn = LocalDateTime.now().plusDays(1);
        LocalDateTime checkOut = LocalDateTime.now().plusDays(3);

        BookingCreateRequest request = new BookingCreateRequest(
            1L, 1L, 1L, checkIn, checkOut, null, null
        );

        RoomEntity room = new RoomEntity();
        room.setCapacity(2);

        when(bookingProperties.getMinNights()).thenReturn(1);
        when(roomRepository.findByIdAndHotelIdAndCityIdWithLock(1L, 1L, 1L)).thenReturn(Optional.of(room));
        when(bookingRepository.existsOverlappingBooking(anyLong(), any(), any(), any())).thenReturn(true);

        assertThatThrownBy(() -> bookingService.initBooking(request))
            .isInstanceOf(ConflictException.class);
    }

    // гостей больше чем вмещает номер — должен выброситься ValidationException
    @Test
    void initBooking_whenGuestsExceedCapacity_throwsValidationException() {
        LocalDateTime checkIn = LocalDateTime.now().plusDays(1);
        LocalDateTime checkOut = LocalDateTime.now().plusDays(3);

        GuestInfoRequest guest = new GuestInfoRequest("Jane", "Doe", "12345678901235", "jane@test.com", "+99891");
        BookingCreateRequest request = new BookingCreateRequest(
            1L, 1L, 1L, checkIn, checkOut, null, List.of(guest, guest)
        );

        RoomEntity room = new RoomEntity();
        room.setCapacity(2); // 1 основной + 2 доп = 3 гостя > вместимость 2

        when(bookingProperties.getMinNights()).thenReturn(1);
        when(roomRepository.findByIdAndHotelIdAndCityIdWithLock(1L, 1L, 1L)).thenReturn(Optional.of(room));
        when(bookingRepository.existsOverlappingBooking(anyLong(), any(), any(), any())).thenReturn(false);

        assertThatThrownBy(() -> bookingService.initBooking(request))
            .isInstanceOf(ValidationException.class);
    }

    // подтверждение бронирования не в статусе HOLD — должен выброситься ConflictException
    @Test
    void confirmBooking_whenStatusIsNotHold_throwsConflictException() {
        BookingConfirmRequest request = new BookingConfirmRequest(1L);

        BookingEntity booking = new BookingEntity();
        booking.setStatus(BookingStatus.CONFIRMED);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.confirmBooking(request))
            .isInstanceOf(ConflictException.class);
    }

    // отмена несуществующего бронирования — должен выброситься NotFoundException
    @Test
    void cancel_whenBookingNotFound_throwsNotFoundException() {
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.cancel(999L))
            .isInstanceOf(NotFoundException.class);
    }
}
