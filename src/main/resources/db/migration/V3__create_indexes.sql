-- =====================================================
-- ИНДЕКСЫ
-- =====================================================

-- cities (1 индекс)
CREATE INDEX idx_cities_not_deleted ON cities(id) WHERE deleted_at IS NULL;

-- hotels (2 индекса)
CREATE INDEX idx_hotels_city ON hotels(city_id);
CREATE INDEX idx_hotels_not_deleted ON hotels(id) WHERE deleted_at IS NULL;

-- rooms (2 индекса)
CREATE INDEX idx_rooms_hotel ON rooms(hotel_id);
CREATE INDEX idx_rooms_not_deleted ON rooms(id) WHERE deleted_at IS NULL;

-- room_price_history (1 индекс)
CREATE INDEX idx_price_history_room_from ON room_price_history(room_id, valid_from);

-- guests (1 индекс)
CREATE INDEX idx_guests_not_deleted ON guests(id) WHERE deleted_at IS NULL;

-- bookings (3 индекса)
CREATE INDEX idx_bookings_room ON bookings(room_id);
CREATE INDEX idx_bookings_guest ON bookings(guest_id);
CREATE INDEX idx_bookings_not_deleted ON bookings(id) WHERE deleted_at IS NULL;

-- booking_additional_guests (1 индекс)
CREATE INDEX idx_additional_guests_guest ON booking_additional_guests(guest_id);

-- hotel_reviews (2 индекса)
CREATE INDEX idx_reviews_hotel ON hotel_reviews(hotel_id);
CREATE INDEX idx_reviews_guest ON hotel_reviews(guest_id);
