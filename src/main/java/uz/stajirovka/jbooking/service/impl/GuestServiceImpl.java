package uz.stajirovka.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.stajirovka.jbooking.constant.enums.Error;
import uz.stajirovka.jbooking.dto.request.GuestCreateRequest;
import uz.stajirovka.jbooking.dto.response.BookingResponse;
import uz.stajirovka.jbooking.dto.response.GuestResponse;
import uz.stajirovka.jbooking.entity.GuestEntity;
import uz.stajirovka.jbooking.exception.ConflictException;
import uz.stajirovka.jbooking.exception.NotFoundException;
import uz.stajirovka.jbooking.mapper.BookingMapper;
import uz.stajirovka.jbooking.mapper.GuestMapper;
import uz.stajirovka.jbooking.repository.BookingRepository;
import uz.stajirovka.jbooking.repository.GuestRepository;
import uz.stajirovka.jbooking.service.GuestService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;
    private final BookingRepository bookingRepository;
    private final GuestMapper guestMapper;
    private final BookingMapper bookingMapper;

    // создание нового гостя
    @Override
    @Transactional
    public GuestResponse create(GuestCreateRequest request) {
        // проверяем уникальность email если он указан
        String email = normalizeEmail(request.email());
        if (email != null && guestRepository.existsByEmail(email)) {
            throw new ConflictException(Error.GUEST_ALREADY_EXISTS, "email=" + email);
        }

        GuestEntity entity = guestMapper.toEntity(request);
        entity.setEmail(email);
        entity.setCreatedAt(LocalDateTime.now());
        return guestMapper.toResponse(guestRepository.save(entity));
    }

    // получение гостя по идентификатору
    @Override
    public GuestResponse getById(Long id) {
        return guestMapper.toResponse(findById(id));
    }

    // получение всех гостей с пагинацией
    @Override
    public Slice<GuestResponse> getAll(Pageable pageable) {
        return guestRepository.findAllBy(pageable)
                .map(guestMapper::toResponse);
    }

    // получение гостя по email
    @Override
    public GuestResponse getByEmail(String email) {
        return guestMapper.toResponse(findByEmail(email));
    }

    // обновление данных гостя
    @Override
    @Transactional
    public GuestResponse update(Long id, GuestCreateRequest request) {
        GuestEntity entity = findById(id);

        // проверяем уникальность email если он изменился
        String newEmail = normalizeEmail(request.email());
        if (newEmail != null && !newEmail.equals(entity.getEmail())
                && guestRepository.existsByEmail(newEmail)) {
            throw new ConflictException(Error.GUEST_ALREADY_EXISTS, "email=" + newEmail);
        }

        entity.setFirstName(request.firstName());
        entity.setLastName(request.lastName());
        entity.setEmail(newEmail);
        entity.setPhone(request.phone());
        return guestMapper.toResponse(entity);
    }

    // мягкое удаление гостя
    @Override
    @Transactional
    public void delete(Long id) {
        GuestEntity entity = findById(id);
        entity.setDeletedAt(LocalDateTime.now());
    }

    // получение бронирований гостя по email
    @Override
    public Slice<BookingResponse> getBookingsByEmail(String email, Pageable pageable) {
        GuestEntity guest = findByEmail(email);
        return bookingRepository.findByGuestId(guest.getId(), pageable)
                .map(bookingMapper::toResponse);
    }

    // поиск гостя по идентификатору или выброс исключения
    private GuestEntity findById(Long id) {
        return guestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Error.GUEST_NOT_FOUND, "id=" + id));
    }

    // поиск гостя по email или выброс исключения
    private GuestEntity findByEmail(String email) {
        return guestRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(Error.GUEST_NOT_FOUND, "email=" + email));
    }

    // нормализация email: пустая строка -> null
    private String normalizeEmail(String email) {
        return email != null && !email.isBlank() ? email : null;
    }
}
