-- =====================================================
-- ТЕСТОВЫЕ ДАННЫЕ
-- =====================================================

-- cities
INSERT INTO cities (name, country)
VALUES ('Ташкент', 'Узбекистан'),
       ('Самарканд', 'Узбекистан'),
       ('Бухара', 'Узбекистан'),
       ('Хива', 'Узбекистан'),
       ('Фергана', 'Узбекистан');

-- hotels
INSERT INTO hotels (city_id, name, description, stars, accommodation_type)
VALUES (1, 'Hyatt Regency Tashkent', 'Роскошный отель в центре столицы', 5, 'HOTEL'),
       (2, 'Silk Road Samarkand', 'Отель у площади Регистан', 4, 'HOTEL'),
       (3, 'Bukhara Palace', 'Исторический отель в старом городе', 4, 'HOTEL'),
       (4, 'Khiva Grand', 'Отель с видом на Ичан-Калу', 3, 'HOTEL'),
       (5, 'Fergana Valley Resort', 'Курортный отель в долине', 4, 'VILLA');

-- =====================================================
-- rooms: Hyatt Regency Tashkent (34)
-- =====================================================
INSERT INTO rooms (hotel_id, room_number, room_type, meal_plan, capacity, price_per_night, description)
VALUES (1, '101', 'STANDARD', 'BREAKFAST', 2, 150000, 'Стандартный номер с завтраком'),
       (1, '102', 'STANDARD', 'BREAKFAST', 2, 150000, 'Стандартный номер'),
       (1, '103', 'STANDARD', 'BREAKFAST', 2, 150000, 'Стандартный номер'),
       (1, '104', 'STANDARD', 'BREAKFAST', 2, 150000, 'Стандартный номер'),
       (1, '105', 'STANDARD', 'HALF_BOARD', 2, 180000, 'Стандартный номер с полупансионом'),
       (1, '106', 'STANDARD', 'ROOM_ONLY', 2, 120000, 'Эконом стандарт'),
       (1, '107', 'STANDARD', 'ROOM_ONLY', 2, 120000, 'Эконом стандарт'),
       (1, '108', 'STANDARD', 'BREAKFAST', 1, 100000, 'Одноместный стандарт'),
       (1, '109', 'STANDARD', 'BREAKFAST', 1, 100000, 'Одноместный стандарт'),
       (1, '201', 'LUXE', 'HALF_BOARD', 2, 300000, 'Люкс с полупансионом'),
       (1, '202', 'STANDARD', 'BREAKFAST', 2, 160000, 'Улучшенный стандарт'),
       (1, '203', 'STANDARD', 'BREAKFAST', 2, 160000, 'Улучшенный стандарт'),
       (1, '204', 'SEMI_LUXE', 'BREAKFAST', 2, 220000, 'Полулюкс'),
       (1, '205', 'SEMI_LUXE', 'HALF_BOARD', 3, 260000, 'Полулюкс семейный'),
       (1, '206', 'SEMI_LUXE', 'BREAKFAST', 2, 220000, 'Полулюкс'),
       (1, '301', 'SEMI_LUXE', 'BREAKFAST', 2, 230000, 'Полулюкс с видом'),
       (1, '302', 'SEMI_LUXE', 'BREAKFAST', 2, 230000, 'Полулюкс с видом'),
       (1, '303', 'LUXE', 'HALF_BOARD', 2, 320000, 'Люкс'),
       (1, '304', 'LUXE', 'HALF_BOARD', 2, 320000, 'Люкс'),
       (1, '305', 'LUXE', 'FULL_BOARD', 3, 380000, 'Люкс семейный'),
       (1, '401', 'LUXE', 'BREAKFAST', 2, 350000, 'Люкс панорамный'),
       (1, '402', 'LUXE', 'BREAKFAST', 2, 350000, 'Люкс панорамный'),
       (1, '403', 'LUXE', 'HALF_BOARD', 2, 380000, 'Люкс делюкс'),
       (1, '404', 'LUXE', 'HALF_BOARD', 2, 380000, 'Люкс делюкс'),
       (1, '405', 'LUXE', 'ALL_INCLUSIVE', 4, 450000, 'Люкс семейный всё включено'),
       (1, '501', 'SUITE', 'HALF_BOARD', 2, 500000, 'Сьют классический'),
       (1, '502', 'SUITE', 'HALF_BOARD', 2, 500000, 'Сьют классический'),
       (1, '503', 'SUITE', 'FULL_BOARD', 3, 580000, 'Сьют семейный'),
       (1, '504', 'SUITE', 'ALL_INCLUSIVE', 4, 650000, 'Сьют премиум'),
       (1, '601', 'SUITE', 'ALL_INCLUSIVE', 2, 600000, 'Сьют роскошный'),
       (1, '602', 'SUITE', 'ALL_INCLUSIVE', 3, 680000, 'Сьют роскошный семейный'),
       (1, '603', 'PRESIDENTIAL', 'ALL_INCLUSIVE', 4, 900000, 'Президентский номер'),
       (1, '701', 'PRESIDENTIAL', 'ALL_INCLUSIVE', 4, 950000, 'Президентский люкс'),
       (1, '702', 'PRESIDENTIAL', 'ALL_INCLUSIVE', 6, 1200000, 'Президентский пентхаус');

-- =====================================================
-- rooms: Silk Road Samarkand (24)
-- =====================================================
INSERT INTO rooms (hotel_id, room_number, room_type, meal_plan, capacity, price_per_night, description)
VALUES (2, '102', 'STANDARD', 'BREAKFAST', 2, 100000, 'Стандартный номер'),
       (2, '103', 'STANDARD', 'BREAKFAST', 2, 100000, 'Стандартный номер'),
       (2, '104', 'STANDARD', 'BREAKFAST', 2, 100000, 'Стандартный номер'),
       (2, '105', 'STANDARD', 'BREAKFAST', 2, 100000, 'Стандартный номер'),
       (2, '106', 'STANDARD', 'ROOM_ONLY', 2, 80000, 'Эконом'),
       (2, '107', 'STANDARD', 'ROOM_ONLY', 1, 60000, 'Одноместный'),
       (2, '108', 'STANDARD', 'BREAKFAST', 2, 95000, 'Стандарт'),
       (2, '109', 'STANDARD', 'BREAKFAST', 2, 95000, 'Стандарт'),
       (2, '202', 'SEMI_LUXE', 'FULL_BOARD', 3, 180000, 'Полулюкс с видом на Регистан'),
       (2, '203', 'STANDARD', 'BREAKFAST', 2, 110000, 'Стандарт улучшенный'),
       (2, '204', 'SEMI_LUXE', 'BREAKFAST', 2, 150000, 'Полулюкс'),
       (2, '205', 'SEMI_LUXE', 'BREAKFAST', 2, 150000, 'Полулюкс'),
       (2, '206', 'SEMI_LUXE', 'HALF_BOARD', 3, 180000, 'Полулюкс семейный'),
       (2, '301', 'SEMI_LUXE', 'BREAKFAST', 2, 160000, 'Полулюкс с видом на Регистан'),
       (2, '302', 'SEMI_LUXE', 'BREAKFAST', 2, 160000, 'Полулюкс с видом на Регистан'),
       (2, '303', 'LUXE', 'HALF_BOARD', 2, 220000, 'Люкс'),
       (2, '304', 'LUXE', 'HALF_BOARD', 2, 220000, 'Люкс'),
       (2, '401', 'LUXE', 'BREAKFAST', 2, 200000, 'Люкс'),
       (2, '402', 'LUXE', 'FULL_BOARD', 3, 280000, 'Люкс семейный'),
       (2, '403', 'SUITE', 'HALF_BOARD', 2, 300000, 'Сьют'),
       (2, '404', 'SUITE', 'ALL_INCLUSIVE', 4, 380000, 'Сьют семейный'),
       (2, '501', 'SUITE', 'ALL_INCLUSIVE', 2, 350000, 'Сьют премиум'),
       (2, '502', 'SUITE', 'ALL_INCLUSIVE', 3, 400000, 'Сьют с террасой'),
       (2, '503', 'PRESIDENTIAL', 'ALL_INCLUSIVE', 4, 550000, 'Президентский номер');

-- =====================================================
-- rooms: Bukhara Palace (20)
-- =====================================================
INSERT INTO rooms (hotel_id, room_number, room_type, meal_plan, capacity, price_per_night, description)
VALUES (3, '103', 'STANDARD', 'ROOM_ONLY', 2, 80000, 'Стандартный номер'),
       (3, '104', 'STANDARD', 'BREAKFAST', 2, 80000, 'Стандартный номер'),
       (3, '105', 'STANDARD', 'BREAKFAST', 2, 80000, 'Стандартный номер'),
       (3, '106', 'STANDARD', 'ROOM_ONLY', 2, 65000, 'Эконом'),
       (3, '107', 'STANDARD', 'ROOM_ONLY', 1, 50000, 'Одноместный'),
       (3, '108', 'STANDARD', 'BREAKFAST', 2, 75000, 'Стандарт'),
       (3, '109', 'STANDARD', 'BREAKFAST', 2, 75000, 'Стандарт'),
       (3, '110', 'STANDARD', 'ROOM_ONLY', 2, 60000, 'Эконом'),
       (3, '111', 'STANDARD', 'ROOM_ONLY', 2, 60000, 'Эконом'),
       (3, '203', 'SUITE', 'ALL_INCLUSIVE', 4, 250000, 'Сьют всё включено'),
       (3, '204', 'STANDARD', 'BREAKFAST', 2, 90000, 'Стандарт улучшенный'),
       (3, '205', 'SEMI_LUXE', 'BREAKFAST', 2, 130000, 'Полулюкс'),
       (3, '206', 'SEMI_LUXE', 'HALF_BOARD', 2, 150000, 'Полулюкс'),
       (3, '207', 'SEMI_LUXE', 'BREAKFAST', 3, 160000, 'Полулюкс семейный'),
       (3, '301', 'LUXE', 'HALF_BOARD', 2, 200000, 'Люкс'),
       (3, '302', 'LUXE', 'HALF_BOARD', 2, 200000, 'Люкс'),
       (3, '303', 'LUXE', 'FULL_BOARD', 3, 250000, 'Люкс семейный'),
       (3, '304', 'SUITE', 'HALF_BOARD', 2, 280000, 'Сьют'),
       (3, '401', 'SUITE', 'ALL_INCLUSIVE', 2, 320000, 'Сьют премиум'),
       (3, '402', 'SUITE', 'ALL_INCLUSIVE', 4, 380000, 'Сьют с видом на старый город');

-- =====================================================
-- rooms: Khiva Grand (12)
-- =====================================================
INSERT INTO rooms (hotel_id, room_number, room_type, meal_plan, capacity, price_per_night, description)
VALUES (4, '104', 'STANDARD', 'BREAKFAST', 2, 70000, 'Стандартный номер'),
       (4, '105', 'STANDARD', 'BREAKFAST', 2, 70000, 'Стандартный номер'),
       (4, '106', 'STANDARD', 'ROOM_ONLY', 2, 55000, 'Эконом'),
       (4, '107', 'STANDARD', 'ROOM_ONLY', 1, 45000, 'Одноместный'),
       (4, '204', 'LUXE', 'HALF_BOARD', 2, 150000, 'Люкс с видом на крепость'),
       (4, '205', 'STANDARD', 'BREAKFAST', 2, 75000, 'Стандарт улучшенный'),
       (4, '206', 'SEMI_LUXE', 'BREAKFAST', 2, 110000, 'Полулюкс'),
       (4, '207', 'SEMI_LUXE', 'HALF_BOARD', 2, 130000, 'Полулюкс'),
       (4, '301', 'LUXE', 'HALF_BOARD', 2, 170000, 'Люкс с видом на Ичан-Калу'),
       (4, '302', 'LUXE', 'HALF_BOARD', 3, 190000, 'Люкс семейный'),
       (4, '303', 'SUITE', 'FULL_BOARD', 2, 220000, 'Сьют'),
       (4, '304', 'SUITE', 'ALL_INCLUSIVE', 4, 280000, 'Сьют панорамный');

-- =====================================================
-- rooms: Fergana Valley Resort (30)
-- =====================================================
INSERT INTO rooms (hotel_id, room_number, room_type, meal_plan, capacity, price_per_night, description)
VALUES (5, '105', 'PRESIDENTIAL', 'ALL_INCLUSIVE', 4, 400000, 'Президентский номер'),
       (5, '106', 'STANDARD', 'BREAKFAST', 2, 120000, 'Стандартный номер'),
       (5, '107', 'STANDARD', 'BREAKFAST', 2, 120000, 'Стандартный номер'),
       (5, '108', 'STANDARD', 'BREAKFAST', 2, 120000, 'Стандартный номер'),
       (5, '109', 'STANDARD', 'HALF_BOARD', 2, 140000, 'Стандарт с полупансионом'),
       (5, '110', 'STANDARD', 'ROOM_ONLY', 2, 100000, 'Эконом'),
       (5, '111', 'STANDARD', 'ROOM_ONLY', 1, 80000, 'Одноместный'),
       (5, '112', 'STANDARD', 'BREAKFAST', 2, 115000, 'Стандарт'),
       (5, '113', 'STANDARD', 'BREAKFAST', 2, 115000, 'Стандарт'),
       (5, '114', 'STANDARD', 'BREAKFAST', 2, 115000, 'Стандарт'),
       (5, '115', 'STANDARD', 'ROOM_ONLY', 2, 95000, 'Эконом'),
       (5, '205', 'STANDARD', 'BREAKFAST', 2, 120000, 'Стандартный номер'),
       (5, '206', 'SEMI_LUXE', 'BREAKFAST', 2, 180000, 'Полулюкс'),
       (5, '207', 'SEMI_LUXE', 'BREAKFAST', 2, 180000, 'Полулюкс'),
       (5, '208', 'SEMI_LUXE', 'HALF_BOARD', 3, 210000, 'Полулюкс семейный'),
       (5, '209', 'SEMI_LUXE', 'HALF_BOARD', 2, 200000, 'Полулюкс с видом на горы'),
       (5, '210', 'SEMI_LUXE', 'FULL_BOARD', 2, 230000, 'Полулюкс полный пансион'),
       (5, '301', 'LUXE', 'HALF_BOARD', 2, 280000, 'Люкс'),
       (5, '302', 'LUXE', 'HALF_BOARD', 2, 280000, 'Люкс'),
       (5, '303', 'LUXE', 'FULL_BOARD', 3, 330000, 'Люкс семейный'),
       (5, '304', 'LUXE', 'FULL_BOARD', 2, 300000, 'Люкс с террасой'),
       (5, '305', 'LUXE', 'ALL_INCLUSIVE', 4, 380000, 'Люкс премиум'),
       (5, '401', 'SUITE', 'HALF_BOARD', 2, 350000, 'Сьют'),
       (5, '402', 'SUITE', 'FULL_BOARD', 3, 400000, 'Сьют семейный'),
       (5, '403', 'SUITE', 'ALL_INCLUSIVE', 4, 480000, 'Сьют премиум'),
       (5, '404', 'SUITE', 'ALL_INCLUSIVE', 2, 420000, 'Сьют с джакузи'),
       (5, 'V01', 'PRESIDENTIAL', 'ALL_INCLUSIVE', 4, 600000, 'Вилла стандарт'),
       (5, 'V02', 'PRESIDENTIAL', 'ALL_INCLUSIVE', 6, 750000, 'Вилла семейная'),
       (5, 'V03', 'PRESIDENTIAL', 'ALL_INCLUSIVE', 6, 800000, 'Вилла с бассейном'),
       (5, 'V04', 'PRESIDENTIAL', 'ALL_INCLUSIVE', 8, 1000000, 'Вилла президентская');

-- =====================================================
-- room_amenities
-- =====================================================

-- Hyatt (5 звёзд): WiFi, TV, кондиционер для всех
INSERT INTO room_amenities (room_id, amenity)
SELECT r.id, 'WIFI' FROM rooms r WHERE r.hotel_id = 1;
INSERT INTO room_amenities (room_id, amenity)
SELECT r.id, 'TV' FROM rooms r WHERE r.hotel_id = 1;
INSERT INTO room_amenities (room_id, amenity)
SELECT r.id, 'AIR_CONDITIONING' FROM rooms r WHERE r.hotel_id = 1;
-- полулюксы и выше: мини-бар
INSERT INTO room_amenities (room_id, amenity)
SELECT r.id, 'MINI_BAR' FROM rooms r
WHERE r.hotel_id = 1 AND r.room_type IN ('SEMI_LUXE', 'LUXE', 'SUITE', 'PRESIDENTIAL');
-- люксы и выше: сейф
INSERT INTO room_amenities (room_id, amenity)
SELECT r.id, 'SAFE' FROM rooms r
WHERE r.hotel_id = 1 AND r.room_type IN ('LUXE', 'SUITE', 'PRESIDENTIAL');

-- Hotel 2-5: базовые удобства
INSERT INTO room_amenities (room_id, amenity)
SELECT r.id, 'WIFI' FROM rooms r WHERE r.hotel_id IN (2, 3, 4, 5);
INSERT INTO room_amenities (room_id, amenity)
SELECT r.id, 'TV' FROM rooms r WHERE r.hotel_id IN (2, 3, 4, 5);
-- кондиционер для полулюксов и выше
INSERT INTO room_amenities (room_id, amenity)
SELECT r.id, 'AIR_CONDITIONING' FROM rooms r
WHERE r.hotel_id IN (2, 3, 4, 5) AND r.room_type IN ('SEMI_LUXE', 'LUXE', 'SUITE', 'PRESIDENTIAL');
-- мини-бар для люксов и выше
INSERT INTO room_amenities (room_id, amenity)
SELECT r.id, 'MINI_BAR' FROM rooms r
WHERE r.hotel_id IN (2, 3, 4, 5) AND r.room_type IN ('LUXE', 'SUITE', 'PRESIDENTIAL');
-- сейф для президентских
INSERT INTO room_amenities (room_id, amenity)
SELECT r.id, 'SAFE' FROM rooms r
WHERE r.hotel_id IN (2, 3, 4, 5) AND r.room_type = 'PRESIDENTIAL';

-- =====================================================
-- room_price_history
-- =====================================================
INSERT INTO room_price_history (room_id, price_per_night, valid_from)
SELECT id, price_per_night, created_at FROM rooms;

-- =====================================================
-- guests (6)
-- =====================================================
INSERT INTO guests (first_name, last_name, pinfl, email, phone, created_at)
VALUES
    ('Осаму', 'Дадзай', '12345678901234', 'osamu.dazai@mail.jp', '+81901112233', CURRENT_TIMESTAMP),
    ('Харуки', 'Мураками', '23456789012345', 'haruki.murakami@mail.jp', '+81902223344', CURRENT_TIMESTAMP),
    ('Ясунари', 'Кавабата', '34567890123456', 'yasunari.kawabata@mail.jp', '+81903334455', CURRENT_TIMESTAMP),
    ('Наоко', 'Кобаяси', '45678901234567', 'naoko.kobayashi@mail.jp', '+81904445566', CURRENT_TIMESTAMP),
    ('Цукуру', 'Тадзаки', '56789012345678', 'tsukuru.tazaki@mail.jp', '+81905556677', CURRENT_TIMESTAMP),
    ('Эминем', 'Эминемович', '67890123456789', 'eminem@mail.com', '+19991234567', CURRENT_TIMESTAMP);

-- =====================================================
-- bookings
-- =====================================================
DO $$
DECLARE
    v_room_id BIGINT;
    v_guest_id BIGINT;
    v_booking_id BIGINT;
BEGIN
    -- Hotel 1: Hyatt (city_id=1)
    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 1 AND room_number = '101';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'osamu.dazai@mail.jp';
    INSERT INTO bookings (city_id, hotel_id, room_id, guest_id, check_in_date, check_out_date, status, price_per_night, total_price, created_at, updated_at)
    VALUES (1, 1, v_room_id, v_guest_id, '2026-03-01 14:00:00', '2026-03-05 12:00:00', 'CONFIRMED', 150000, 600000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 1 AND room_number = '201';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'haruki.murakami@mail.jp';
    INSERT INTO bookings (city_id, hotel_id, room_id, guest_id, check_in_date, check_out_date, status, price_per_night, total_price, created_at, updated_at)
    VALUES (1, 1, v_room_id, v_guest_id, '2026-03-10 14:00:00', '2026-03-15 12:00:00', 'CONFIRMED', 300000, 1500000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 1 AND room_number = '303';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'yasunari.kawabata@mail.jp';
    INSERT INTO bookings (city_id, hotel_id, room_id, guest_id, check_in_date, check_out_date, status, price_per_night, total_price, created_at, updated_at)
    VALUES (1, 1, v_room_id, v_guest_id, '2026-03-20 14:00:00', '2026-03-25 12:00:00', 'HOLD', 320000, 1600000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 1 AND room_number = '501';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'naoko.kobayashi@mail.jp';
    INSERT INTO bookings (city_id, hotel_id, room_id, guest_id, check_in_date, check_out_date, status, price_per_night, total_price, created_at, updated_at)
    VALUES (1, 1, v_room_id, v_guest_id, '2026-04-01 14:00:00', '2026-04-05 12:00:00', 'CONFIRMED', 500000, 2000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 1 AND room_number = '603';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'tsukuru.tazaki@mail.jp';
    INSERT INTO bookings (city_id, hotel_id, room_id, guest_id, check_in_date, check_out_date, status, price_per_night, total_price, created_at, updated_at)
    VALUES (1, 1, v_room_id, v_guest_id, '2026-04-10 14:00:00', '2026-04-15 12:00:00', 'CONFIRMED', 900000, 4500000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 1 AND room_number = '702';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'eminem@mail.com';
    INSERT INTO bookings (city_id, hotel_id, room_id, guest_id, check_in_date, check_out_date, status, price_per_night, total_price, created_at, updated_at)
    VALUES (1, 1, v_room_id, v_guest_id, '2026-04-20 14:00:00', '2026-04-27 12:00:00', 'CONFIRMED', 1200000, 8400000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
    RETURNING id INTO v_booking_id;
    INSERT INTO booking_additional_guests (booking_id, guest_id)
    SELECT v_booking_id, id FROM guests WHERE email = 'osamu.dazai@mail.jp';
    INSERT INTO booking_additional_guests (booking_id, guest_id)
    SELECT v_booking_id, id FROM guests WHERE email = 'haruki.murakami@mail.jp';

    -- Hotel 2: Silk Road Samarkand (city_id=2)
    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 2 AND room_number = '301';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'osamu.dazai@mail.jp';
    INSERT INTO bookings (city_id, hotel_id, room_id, guest_id, check_in_date, check_out_date, status, price_per_night, total_price, created_at, updated_at)
    VALUES (2, 2, v_room_id, v_guest_id, '2026-05-01 14:00:00', '2026-05-04 12:00:00', 'CONFIRMED', 160000, 480000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 2 AND room_number = '403';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'haruki.murakami@mail.jp';
    INSERT INTO bookings (city_id, hotel_id, room_id, guest_id, check_in_date, check_out_date, status, price_per_night, total_price, created_at, updated_at)
    VALUES (2, 2, v_room_id, v_guest_id, '2026-05-10 14:00:00', '2026-05-15 12:00:00', 'HOLD', 300000, 1500000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 2 AND room_number = '503';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'yasunari.kawabata@mail.jp';
    INSERT INTO bookings (city_id, hotel_id, room_id, guest_id, check_in_date, check_out_date, status, price_per_night, total_price, created_at, updated_at)
    VALUES (2, 2, v_room_id, v_guest_id, '2026-05-20 14:00:00', '2026-05-25 12:00:00', 'CONFIRMED', 550000, 2750000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    -- Hotel 3: Bukhara Palace (city_id=3)
    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 3 AND room_number = '301';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'naoko.kobayashi@mail.jp';
    INSERT INTO bookings (city_id, hotel_id, room_id, guest_id, check_in_date, check_out_date, status, price_per_night, total_price, created_at, updated_at)
    VALUES (3, 3, v_room_id, v_guest_id, '2026-06-01 14:00:00', '2026-06-05 12:00:00', 'CONFIRMED', 200000, 800000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 3 AND room_number = '401';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'tsukuru.tazaki@mail.jp';
    INSERT INTO bookings (city_id, hotel_id, room_id, guest_id, check_in_date, check_out_date, status, price_per_night, total_price, created_at, updated_at)
    VALUES (3, 3, v_room_id, v_guest_id, '2026-06-10 14:00:00', '2026-06-14 12:00:00', 'HOLD', 320000, 1280000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    -- Hotel 4: Khiva Grand (city_id=4)
    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 4 AND room_number = '304';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'eminem@mail.com';
    INSERT INTO bookings (city_id, hotel_id, room_id, guest_id, check_in_date, check_out_date, status, price_per_night, total_price, created_at, updated_at)
    VALUES (4, 4, v_room_id, v_guest_id, '2026-06-20 14:00:00', '2026-06-25 12:00:00', 'CONFIRMED', 280000, 1400000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 4 AND room_number = '301';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'osamu.dazai@mail.jp';
    INSERT INTO bookings (city_id, hotel_id, room_id, guest_id, check_in_date, check_out_date, status, price_per_night, total_price, created_at, updated_at)
    VALUES (4, 4, v_room_id, v_guest_id, '2026-07-01 14:00:00', '2026-07-05 12:00:00', 'CONFIRMED', 170000, 680000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    -- Hotel 5: Fergana Valley Resort (city_id=5)
    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 5 AND room_number = '403';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'haruki.murakami@mail.jp';
    INSERT INTO bookings (city_id, hotel_id, room_id, guest_id, check_in_date, check_out_date, status, price_per_night, total_price, created_at, updated_at)
    VALUES (5, 5, v_room_id, v_guest_id, '2026-07-10 14:00:00', '2026-07-15 12:00:00', 'CONFIRMED', 480000, 2400000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 5 AND room_number = 'V02';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'yasunari.kawabata@mail.jp';
    INSERT INTO bookings (city_id, hotel_id, room_id, guest_id, check_in_date, check_out_date, status, price_per_night, total_price, created_at, updated_at)
    VALUES (5, 5, v_room_id, v_guest_id, '2026-07-20 14:00:00', '2026-07-27 12:00:00', 'CONFIRMED', 750000, 5250000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
    RETURNING id INTO v_booking_id;
    INSERT INTO booking_additional_guests (booking_id, guest_id)
    SELECT v_booking_id, id FROM guests WHERE email = 'naoko.kobayashi@mail.jp';
    INSERT INTO booking_additional_guests (booking_id, guest_id)
    SELECT v_booking_id, id FROM guests WHERE email = 'tsukuru.tazaki@mail.jp';

    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 5 AND room_number = 'V04';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'tsukuru.tazaki@mail.jp';
    INSERT INTO bookings (city_id, hotel_id, room_id, guest_id, check_in_date, check_out_date, status, price_per_night, total_price, created_at, updated_at)
    VALUES (5, 5, v_room_id, v_guest_id, '2026-08-01 14:00:00', '2026-08-10 12:00:00', 'HOLD', 1000000, 9000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
    RETURNING id INTO v_booking_id;
    INSERT INTO booking_additional_guests (booking_id, guest_id)
    SELECT v_booking_id, id FROM guests WHERE email = 'osamu.dazai@mail.jp';
    INSERT INTO booking_additional_guests (booking_id, guest_id)
    SELECT v_booking_id, id FROM guests WHERE email = 'haruki.murakami@mail.jp';
    INSERT INTO booking_additional_guests (booking_id, guest_id)
    SELECT v_booking_id, id FROM guests WHERE email = 'yasunari.kawabata@mail.jp';
    INSERT INTO booking_additional_guests (booking_id, guest_id)
    SELECT v_booking_id, id FROM guests WHERE email = 'naoko.kobayashi@mail.jp';
    INSERT INTO booking_additional_guests (booking_id, guest_id)
    SELECT v_booking_id, id FROM guests WHERE email = 'eminem@mail.com';
END $$;
