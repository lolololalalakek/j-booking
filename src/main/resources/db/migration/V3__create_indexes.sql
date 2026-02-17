-- =====================================================
-- ИНДЕКСЫ
-- =====================================================

-- cities (1 индекс)
CREATE UNIQUE INDEX uk_cities_name_country ON cities(name, country) WHERE deleted_at IS NULL;

-- hotels (1 индекс)
CREATE INDEX idx_hotels_city ON hotels(city_id);

-- rooms (1 индекс, hotel_id уже в PK)
CREATE UNIQUE INDEX uk_rooms_hotel_room_number ON rooms(hotel_id, room_number) WHERE deleted_at IS NULL;

-- room_price_history (1 индекс)
CREATE INDEX idx_price_history_room_from ON room_price_history(room_id, valid_from);

-- guests (1 индекс)
CREATE UNIQUE INDEX uk_guests_pinfl ON guests(pinfl) WHERE pinfl IS NOT NULL AND deleted_at IS NULL;

-- bookings (2 индекса)
CREATE INDEX idx_bookings_hotel_room ON bookings(hotel_id, room_id);
CREATE INDEX idx_bookings_guest ON bookings(guest_id);

-- booking_additional_guests (1 индекс)
CREATE INDEX idx_additional_guests_guest ON booking_additional_guests(guest_id);

-- payment_transactions (3 индекса)
CREATE INDEX idx_payment_transactions_booking ON payment_transactions(booking_id);
CREATE UNIQUE INDEX uk_payment_transactions_txn_id ON payment_transactions(transaction_id);
CREATE UNIQUE INDEX uk_payment_transactions_booking_success ON payment_transactions(booking_id) WHERE status = 'CREATED';

-- hotel_reviews (1 индекс)
CREATE UNIQUE INDEX uk_hotel_reviews_hotel_guest ON hotel_reviews(hotel_id, guest_id);
