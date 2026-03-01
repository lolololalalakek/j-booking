package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.component.properties.BookingProperties;
import uz.stajirovka.jbooking.constant.enums.BookingStatus;
import uz.stajirovka.jbooking.constant.enums.Currency;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.dto.request.BookingConfirmRequest;
import uz.stajirovka.jbooking.dto.request.BookingCreateRequest;
import uz.stajirovka.jbooking.dto.request.BookingPaymentRequest;
import uz.stajirovka.jbooking.dto.request.GuestInfoRequest;
import uz.stajirovka.jbooking.dto.request.PaymentRequest;
import uz.stajirovka.jbooking.dto.response.BookingConfirmResponse;
import uz.stajirovka.jbooking.dto.response.BookingResponse;
import uz.stajirovka.jbooking.dto.response.PaymentResponse;
import uz.stajirovka.jbooking.entity.BookingEntity;
import uz.stajirovka.jbooking.entity.GuestEntity;
import uz.stajirovka.jbooking.entity.RoomEntity;
import uz.stajirovka.jbooking.exception.ConflictException;
import uz.stajirovka.jbooking.exception.NotFoundException;
import uz.stajirovka.jbooking.exception.ValidationException;
import uz.stajirovka.jbooking.mapper.BookingMapper;
import uz.stajirovka.jbooking.repository.BookingRepository;
import uz.stajirovka.jbooking.repository.RoomRepository;
import uz.stajirovka.jbooking.service.BookingPriceService;
import uz.stajirovka.jbooking.service.BookingService;
import uz.stajirovka.jbooking.service.CurrencyConverterService;
import uz.stajirovka.jbooking.service.GuestService;
import uz.stajirovka.jbooking.service.NotificationProcessorService;
import uz.stajirovka.jbooking.service.PaymentExecutor;
import uz.stajirovka.jbooking.utility.BookingDateValidator;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final GuestService guestService;
    private final BookingMapper bookingMapper;
    private final BookingPriceService bookingPriceService;
    private final CurrencyConverterService currencyConverterService;
    private final BookingProperties bookingProperties;
    private final PaymentExecutor paymentExecutor;
    private final PaymentRequestValidationService paymentRequestValidationService;
    private final PaymentResultHandlerService paymentResultHandlerService;
    private final NotificationProcessorService notificationProcessorService;

    @Override
    @Transactional // инициализация букинга
    public BookingResponse initBooking(BookingCreateRequest request) {
        long nights = BookingDateValidator.validateAndCalculateNights(
            request.checkInDate(), request.checkOutDate(), bookingProperties.getMinNights());

        RoomEntity room = findRoomWithLock(request.cityId(), request.hotelId(), request.roomId());

        if (bookingRepository.existsOverlappingBooking(
            request.roomId(), request.checkInDate(), request.checkOutDate(), BookingStatus.CANCELLED)) {
            throw new ConflictException(Error.ROOM_NOT_AVAILABLE);
        }
        //проставляем кол-во гостей. по дефолту всегда 1 + доп гости (если будут указаны)
        int totalGuests = 1 + (request.additionalGuests() != null ? request.additionalGuests().size() : 0);
        if (totalGuests > room.getCapacity()) {
            throw new ValidationException(Error.VALIDATION_ERROR,
                "Guest count (" + totalGuests + ") exceeds room capacity (" + room.getCapacity() + ")");
        }
        // находим или создаем основного гостя
        GuestEntity mainGuest = guestService.findOrCreateGuest(request.mainGuest());

        // здесь мы создаем для доп гостей хеш-сет.
        Set<GuestEntity> additionalGuests = new HashSet<>();
        if (request.additionalGuests() != null) {
            for (GuestInfoRequest guestInfo : request.additionalGuests()) {
                additionalGuests.add(guestService.findOrCreateGuest(guestInfo));
            }
        }

        Long totalPrice = bookingPriceService.calculate(room, nights);
        LocalDateTime now = LocalDateTime.now();
        BookingEntity booking = bookingMapper.toEntity(
            request, room, mainGuest, additionalGuests, totalPrice, totalGuests, now);

        return bookingMapper.toResponse(
            bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingConfirmResponse confirmBooking(BookingConfirmRequest request) {
        BookingEntity booking = bookingRepository.findById(request.bookingId())
            .orElseThrow(() -> new NotFoundException(Error.BOOKING_NOT_FOUND));

        if (booking.getStatus() != BookingStatus.HOLD) {
            throw new ConflictException(Error.INVALID_BOOKING_STATUS,
                "Transition " + booking.getStatus() + " -> CONFIRMED is not allowed");
        }
        booking.setStatus(BookingStatus.CONFIRMED);
        BookingEntity updated = bookingRepository.save(booking);
        return new BookingConfirmResponse(updated.getStatus(), updated.getId(), updated.getPaymentId());
    }

    @Override
    @Transactional
    public BookingConfirmResponse payConfirmedBooking(BookingPaymentRequest request) {
        Long bookingId = request.bookingId();
        BookingEntity booking = bookingRepository.findByIdAndStatus(bookingId, BookingStatus.CONFIRMED)
            .orElseThrow(() -> new NotFoundException(Error.BOOKING_NOT_FOUND));

        PaymentRequest paymentRequest = paymentRequestValidationService.validate(booking, request);
        if (paymentRequest == null) {
            return new BookingConfirmResponse(BookingStatus.CANCELLED, bookingId, null);
        }

        PaymentResponse paymentResponse;
        try {
            paymentResponse = paymentExecutor.executePayment(paymentRequest);
        } catch (Exception e) {
            log.error("Payment service error for bookingId={}: {}", bookingId, e.getMessage());
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);
            return new BookingConfirmResponse(BookingStatus.CANCELLED, bookingId, null);
        }

        BookingEntity updated = paymentResultHandlerService.handle(booking, paymentResponse);
        if (updated.getStatus() == BookingStatus.PAID) {
            notificationProcessorService.process(updated);
        }
        bookingRepository.save(updated);

        return new BookingConfirmResponse(updated.getStatus(), bookingId, paymentResponse.id());
    }

    @Override
    public BookingResponse cancel(Long bookingId) {
        BookingEntity booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new NotFoundException(Error.BOOKING_NOT_FOUND, "id=" + bookingId));
        booking.setStatus(BookingStatus.CANCELLED);
        BookingEntity updated = bookingRepository.save(booking);
        return bookingMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getById(Long id, Currency currency) {
        return bookingMapper.withConvertedPrices(bookingMapper.toResponse(findById(id)), currency, currencyConverterService);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<BookingResponse> getAllByPinfl(String pinfl, Currency currency, Pageable pageable) {
        return bookingRepository.findByGuestPinfl(pinfl, pageable)
            .map(bookingMapper::toResponse)
            .map(r -> bookingMapper.withConvertedPrices(r, currency, currencyConverterService));
    }

    private BookingEntity findById(Long id) {
        return bookingRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(Error.BOOKING_NOT_FOUND, "id=" + id));
    }

    private RoomEntity findRoomWithLock(Long cityId, Long hotelId, Long roomId) {
        return roomRepository.findByIdAndHotelIdAndCityIdWithLock(roomId, hotelId, cityId)
            .orElseThrow(() -> new NotFoundException(Error.ROOM_NOT_FOUND,
                "cityId=" + cityId + ", hotelId=" + hotelId + ", roomId=" + roomId));
    }
}
