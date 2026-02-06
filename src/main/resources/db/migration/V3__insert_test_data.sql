-- Города Узбекистана
INSERT INTO cities (name, country) VALUES
('Tashkent', 'Uzbekistan'),
('Samarkand', 'Uzbekistan'),
('Bukhara', 'Uzbekistan'),
('Khiva', 'Uzbekistan'),
('Fergana', 'Uzbekistan');

-- Отели (5 штук)
INSERT INTO hotels (city_id, name, description, stars, accommodation_type, brand) VALUES
(1, 'Hyatt Regency Tashkent', 'Luxury hotel in the heart of Tashkent', 5.0, 'HOTEL', 'Hyatt'),
(2, 'Silk Road Samarkand', 'Resort complex near Registan Square', 5.0, 'HOTEL', NULL),
(3, 'Lyabi House Hotel', 'Historic hotel near Lyabi-Hauz', 4.0, 'HOTEL', NULL),
(4, 'Asia Khiva Hotel', 'Inside Itchan Kala fortress', 4.0, 'HOTEL', NULL),
(5, 'Asia Fergana Hotel', 'Best hotel in Fergana Valley', 4.0, 'HOTEL', NULL);

-- Комнаты
INSERT INTO rooms (hotel_id, room_number, room_type, meal_plan, capacity, price_per_night, description) VALUES
-- Hyatt Regency Tashkent
(1, '101', 'STANDARD', 'BREAKFAST', 2, 150.00, 'Standard room with city view'),
(1, '201', 'LUXE', 'HALF_BOARD', 2, 300.00, 'Luxury suite with balcony'),
-- Silk Road Samarkand
(2, '101', 'LUXE', 'ALL_INCLUSIVE', 2, 350.00, 'Registan view room'),
(2, '102', 'STANDARD', 'BREAKFAST', 2, 180.00, 'Garden view room'),
-- Lyabi House Hotel
(3, '101', 'STANDARD', 'BREAKFAST', 2, 90.00, 'Traditional Bukhara style'),
(3, '201', 'SEMI_LUXE', 'FULL_BOARD', 2, 200.00, 'Lyabi-Hauz view'),
-- Asia Khiva Hotel
(4, '101', 'LUXE', 'HALF_BOARD', 2, 160.00, 'Inside ancient fortress'),
(4, '102', 'STANDARD', 'BREAKFAST', 2, 80.00, 'Cozy room'),
-- Asia Fergana Hotel
(5, '101', 'STANDARD', 'BREAKFAST', 2, 70.00, 'Valley view'),
(5, '201', 'SEMI_LUXE', 'HALF_BOARD', 2, 120.00, 'Mountain view');

-- Amenities для комнат
INSERT INTO room_amenities (room_id, amenity) VALUES
(1, 'WIFI'), (1, 'TV'), (1, 'AIR_CONDITIONING'),
(2, 'WIFI'), (2, 'TV'), (2, 'AIR_CONDITIONING'), (2, 'MINI_BAR'), (2, 'SAFE'),
(3, 'WIFI'), (3, 'TV'), (3, 'AIR_CONDITIONING'), (3, 'MINI_BAR'),
(4, 'WIFI'), (4, 'TV'), (4, 'AIR_CONDITIONING'),
(5, 'WIFI'), (5, 'TV'),
(6, 'WIFI'), (6, 'TV'), (6, 'AIR_CONDITIONING'), (6, 'MINI_BAR'),
(7, 'WIFI'), (7, 'TV'), (7, 'AIR_CONDITIONING'), (7, 'SAFE'),
(8, 'WIFI'), (8, 'TV'),
(9, 'WIFI'), (9, 'TV'), (9, 'AIR_CONDITIONING'),
(10, 'WIFI'), (10, 'TV'), (10, 'AIR_CONDITIONING');

-- Бронирования
INSERT INTO bookings (room_id, guest_first_name, guest_last_name, guest_email, number_of_guests, check_in_date, check_out_date, status, total_price, created_at, updated_at) VALUES
(2, 'Haruki', 'Murakami', 'murakami@kafka.jp', 2, '2026-03-01 14:00:00', '2026-03-05 12:00:00', 'CONFIRMED', 1200.00, NOW(), NOW()),
(3, 'Kenzaburo', 'Oe', 'oe@nobel.jp', 2, '2026-03-10 14:00:00', '2026-03-14 12:00:00', 'HOLD', 1400.00, NOW(), NOW()),
(5, 'Yukio', 'Mishima', 'mishima@temple.jp', 2, '2026-04-01 14:00:00', '2026-04-03 12:00:00', 'CONFIRMED', 180.00, NOW(), NOW()),
(7, 'Yasunari', 'Kawabata', 'kawabata@snow.jp', 2, '2026-04-15 14:00:00', '2026-04-18 12:00:00', 'PAID', 480.00, NOW(), NOW()),
(9, 'Osama', 'Dadzai', 'Dadzai@kitchen.jp', 1, '2026-05-01 14:00:00', '2026-05-04 12:00:00', 'HOLD', 210.00, NOW(), NOW());
