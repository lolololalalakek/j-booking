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
VALUES (1, '101', 'STANDARD', 'BREAKFAST', 2, 150.00, 'Стандартный номер с завтраком'),
       (1, '102', 'STANDARD', 'BREAKFAST', 2, 150.00, 'Стандартный номер'),
       (1, '103', 'STANDARD', 'BREAKFAST', 2, 150.00, 'Стандартный номер'),
       (1, '104', 'STANDARD', 'BREAKFAST', 2, 150.00, 'Стандартный номер'),
       (1, '105', 'STANDARD', 'HALF_BOARD', 2, 180.00, 'Стандартный номер с полупансионом'),
       (1, '106', 'STANDARD', 'ROOM_ONLY', 2, 120.00, 'Эконом стандарт'),
       (1, '107', 'STANDARD', 'ROOM_ONLY', 2, 120.00, 'Эконом стандарт'),
       (1, '108', 'STANDARD', 'BREAKFAST', 1, 100.00, 'Одноместный стандарт'),
       (1, '109', 'STANDARD', 'BREAKFAST', 1, 100.00, 'Одноместный стандарт'),
       (1, '201', 'LUXE', 'HALF_BOARD', 2, 300.00, 'Люкс с полупансионом'),
       (1, '202', 'STANDARD', 'BREAKFAST', 2, 160.00, 'Улучшенный стандарт'),
       (1, '203', 'STANDARD', 'BREAKFAST', 2, 160.00, 'Улучшенный стандарт'),
       (1, '204', 'SEMI_LUXE', 'BREAKFAST', 2, 220.00, 'Полулюкс'),
       (1, '205', 'SEMI_LUXE', 'HALF_BOARD', 3, 260.00, 'Полулюкс семейный'),
       (1, '206', 'SEMI_LUXE', 'BREAKFAST', 2, 220.00, 'Полулюкс'),
       (1, '301', 'SEMI_LUXE', 'BREAKFAST', 2, 230.00, 'Полулюкс с видом'),
       (1, '302', 'SEMI_LUXE', 'BREAKFAST', 2, 230.00, 'Полулюкс с видом'),
       (1, '303', 'LUXE', 'HALF_BOARD', 2, 320.00, 'Люкс'),
       (1, '304', 'LUXE', 'HALF_BOARD', 2, 320.00, 'Люкс'),
       (1, '305', 'LUXE', 'FULL_BOARD', 3, 380.00, 'Люкс семейный'),
       (1, '401', 'LUXE', 'BREAKFAST', 2, 350.00, 'Люкс панорамный'),
       (1, '402', 'LUXE', 'BREAKFAST', 2, 350.00, 'Люкс панорамный'),
       (1, '403', 'LUXE', 'HALF_BOARD', 2, 380.00, 'Люкс делюкс'),
       (1, '404', 'LUXE', 'HALF_BOARD', 2, 380.00, 'Люкс делюкс'),
       (1, '405', 'LUXE', 'ALL_INCLUSIVE', 4, 450.00, 'Люкс семейный всё включено'),
       (1, '501', 'SUITE', 'HALF_BOARD', 2, 500.00, 'Сьют классический'),
       (1, '502', 'SUITE', 'HALF_BOARD', 2, 500.00, 'Сьют классический'),
       (1, '503', 'SUITE', 'FULL_BOARD', 3, 580.00, 'Сьют семейный'),
       (1, '504', 'SUITE', 'ALL_INCLUSIVE', 4, 650.00, 'Сьют премиум'),
       (1, '601', 'SUITE', 'ALL_INCLUSIVE', 2, 600.00, 'Сьют роскошный'),
       (1, '602', 'SUITE', 'ALL_INCLUSIVE', 3, 680.00, 'Сьют роскошный семейный'),
       (1, '603', 'PRESIDENTIAL', 'ALL_INCLUSIVE', 4, 900.00, 'Президентский номер'),
       (1, '701', 'PRESIDENTIAL', 'ALL_INCLUSIVE', 4, 950.00, 'Президентский люкс'),
       (1, '702', 'PRESIDENTIAL', 'ALL_INCLUSIVE', 6, 1200.00, 'Президентский пентхаус');

-- =====================================================
-- rooms: Silk Road Samarkand (24)
-- =====================================================
INSERT INTO rooms (hotel_id, room_number, room_type, meal_plan, capacity, price_per_night, description)
VALUES (2, '102', 'STANDARD', 'BREAKFAST', 2, 100.00, 'Стандартный номер'),
       (2, '103', 'STANDARD', 'BREAKFAST', 2, 100.00, 'Стандартный номер'),
       (2, '104', 'STANDARD', 'BREAKFAST', 2, 100.00, 'Стандартный номер'),
       (2, '105', 'STANDARD', 'BREAKFAST', 2, 100.00, 'Стандартный номер'),
       (2, '106', 'STANDARD', 'ROOM_ONLY', 2, 80.00, 'Эконом'),
       (2, '107', 'STANDARD', 'ROOM_ONLY', 1, 60.00, 'Одноместный'),
       (2, '108', 'STANDARD', 'BREAKFAST', 2, 95.00, 'Стандарт'),
       (2, '109', 'STANDARD', 'BREAKFAST', 2, 95.00, 'Стандарт'),
       (2, '202', 'SEMI_LUXE', 'FULL_BOARD', 3, 180.00, 'Полулюкс с видом на Регистан'),
       (2, '203', 'STANDARD', 'BREAKFAST', 2, 110.00, 'Стандарт улучшенный'),
       (2, '204', 'SEMI_LUXE', 'BREAKFAST', 2, 150.00, 'Полулюкс'),
       (2, '205', 'SEMI_LUXE', 'BREAKFAST', 2, 150.00, 'Полулюкс'),
       (2, '206', 'SEMI_LUXE', 'HALF_BOARD', 3, 180.00, 'Полулюкс семейный'),
       (2, '301', 'SEMI_LUXE', 'BREAKFAST', 2, 160.00, 'Полулюкс с видом на Регистан'),
       (2, '302', 'SEMI_LUXE', 'BREAKFAST', 2, 160.00, 'Полулюкс с видом на Регистан'),
       (2, '303', 'LUXE', 'HALF_BOARD', 2, 220.00, 'Люкс'),
       (2, '304', 'LUXE', 'HALF_BOARD', 2, 220.00, 'Люкс'),
       (2, '401', 'LUXE', 'BREAKFAST', 2, 200.00, 'Люкс'),
       (2, '402', 'LUXE', 'FULL_BOARD', 3, 280.00, 'Люкс семейный'),
       (2, '403', 'SUITE', 'HALF_BOARD', 2, 300.00, 'Сьют'),
       (2, '404', 'SUITE', 'ALL_INCLUSIVE', 4, 380.00, 'Сьют семейный'),
       (2, '501', 'SUITE', 'ALL_INCLUSIVE', 2, 350.00, 'Сьют премиум'),
       (2, '502', 'SUITE', 'ALL_INCLUSIVE', 3, 400.00, 'Сьют с террасой'),
       (2, '503', 'PRESIDENTIAL', 'ALL_INCLUSIVE', 4, 550.00, 'Президентский номер');

-- =====================================================
-- rooms: Bukhara Palace (20)
-- =====================================================
INSERT INTO rooms (hotel_id, room_number, room_type, meal_plan, capacity, price_per_night, description)
VALUES (3, '103', 'STANDARD', 'ROOM_ONLY', 2, 80.00, 'Стандартный номер'),
       (3, '104', 'STANDARD', 'BREAKFAST', 2, 80.00, 'Стандартный номер'),
       (3, '105', 'STANDARD', 'BREAKFAST', 2, 80.00, 'Стандартный номер'),
       (3, '106', 'STANDARD', 'ROOM_ONLY', 2, 65.00, 'Эконом'),
       (3, '107', 'STANDARD', 'ROOM_ONLY', 1, 50.00, 'Одноместный'),
       (3, '108', 'STANDARD', 'BREAKFAST', 2, 75.00, 'Стандарт'),
       (3, '109', 'STANDARD', 'BREAKFAST', 2, 75.00, 'Стандарт'),
       (3, '110', 'STANDARD', 'ROOM_ONLY', 2, 60.00, 'Эконом'),
       (3, '111', 'STANDARD', 'ROOM_ONLY', 2, 60.00, 'Эконом'),
       (3, '203', 'SUITE', 'ALL_INCLUSIVE', 4, 250.00, 'Сьют всё включено'),
       (3, '204', 'STANDARD', 'BREAKFAST', 2, 90.00, 'Стандарт улучшенный'),
       (3, '205', 'SEMI_LUXE', 'BREAKFAST', 2, 130.00, 'Полулюкс'),
       (3, '206', 'SEMI_LUXE', 'HALF_BOARD', 2, 150.00, 'Полулюкс'),
       (3, '207', 'SEMI_LUXE', 'BREAKFAST', 3, 160.00, 'Полулюкс семейный'),
       (3, '301', 'LUXE', 'HALF_BOARD', 2, 200.00, 'Люкс'),
       (3, '302', 'LUXE', 'HALF_BOARD', 2, 200.00, 'Люкс'),
       (3, '303', 'LUXE', 'FULL_BOARD', 3, 250.00, 'Люкс семейный'),
       (3, '304', 'SUITE', 'HALF_BOARD', 2, 280.00, 'Сьют'),
       (3, '401', 'SUITE', 'ALL_INCLUSIVE', 2, 320.00, 'Сьют премиум'),
       (3, '402', 'SUITE', 'ALL_INCLUSIVE', 4, 380.00, 'Сьют с видом на старый город');

-- =====================================================
-- rooms: Khiva Grand (12)
-- =====================================================
INSERT INTO rooms (hotel_id, room_number, room_type, meal_plan, capacity, price_per_night, description)
VALUES (4, '104', 'STANDARD', 'BREAKFAST', 2, 70.00, 'Стандартный номер'),
       (4, '105', 'STANDARD', 'BREAKFAST', 2, 70.00, 'Стандартный номер'),
       (4, '106', 'STANDARD', 'ROOM_ONLY', 2, 55.00, 'Эконом'),
       (4, '107', 'STANDARD', 'ROOM_ONLY', 1, 45.00, 'Одноместный'),
       (4, '204', 'LUXE', 'HALF_BOARD', 2, 150.00, 'Люкс с видом на крепость'),
       (4, '205', 'STANDARD', 'BREAKFAST', 2, 75.00, 'Стандарт улучшенный'),
       (4, '206', 'SEMI_LUXE', 'BREAKFAST', 2, 110.00, 'Полулюкс'),
       (4, '207', 'SEMI_LUXE', 'HALF_BOARD', 2, 130.00, 'Полулюкс'),
       (4, '301', 'LUXE', 'HALF_BOARD', 2, 170.00, 'Люкс с видом на Ичан-Калу'),
       (4, '302', 'LUXE', 'HALF_BOARD', 3, 190.00, 'Люкс семейный'),
       (4, '303', 'SUITE', 'FULL_BOARD', 2, 220.00, 'Сьют'),
       (4, '304', 'SUITE', 'ALL_INCLUSIVE', 4, 280.00, 'Сьют панорамный');

-- =====================================================
-- rooms: Fergana Valley Resort (30)
-- =====================================================
INSERT INTO rooms (hotel_id, room_number, room_type, meal_plan, capacity, price_per_night, description)
VALUES (5, '105', 'PRESIDENTIAL', 'ALL_INCLUSIVE', 4, 400.00, 'Президентский номер'),
       (5, '106', 'STANDARD', 'BREAKFAST', 2, 120.00, 'Стандартный номер'),
       (5, '107', 'STANDARD', 'BREAKFAST', 2, 120.00, 'Стандартный номер'),
       (5, '108', 'STANDARD', 'BREAKFAST', 2, 120.00, 'Стандартный номер'),
       (5, '109', 'STANDARD', 'HALF_BOARD', 2, 140.00, 'Стандарт с полупансионом'),
       (5, '110', 'STANDARD', 'ROOM_ONLY', 2, 100.00, 'Эконом'),
       (5, '111', 'STANDARD', 'ROOM_ONLY', 1, 80.00, 'Одноместный'),
       (5, '112', 'STANDARD', 'BREAKFAST', 2, 115.00, 'Стандарт'),
       (5, '113', 'STANDARD', 'BREAKFAST', 2, 115.00, 'Стандарт'),
       (5, '114', 'STANDARD', 'BREAKFAST', 2, 115.00, 'Стандарт'),
       (5, '115', 'STANDARD', 'ROOM_ONLY', 2, 95.00, 'Эконом'),
       (5, '205', 'STANDARD', 'BREAKFAST', 2, 120.00, 'Стандартный номер'),
       (5, '206', 'SEMI_LUXE', 'BREAKFAST', 2, 180.00, 'Полулюкс'),
       (5, '207', 'SEMI_LUXE', 'BREAKFAST', 2, 180.00, 'Полулюкс'),
       (5, '208', 'SEMI_LUXE', 'HALF_BOARD', 3, 210.00, 'Полулюкс семейный'),
       (5, '209', 'SEMI_LUXE', 'HALF_BOARD', 2, 200.00, 'Полулюкс с видом на горы'),
       (5, '210', 'SEMI_LUXE', 'FULL_BOARD', 2, 230.00, 'Полулюкс полный пансион'),
       (5, '301', 'LUXE', 'HALF_BOARD', 2, 280.00, 'Люкс'),
       (5, '302', 'LUXE', 'HALF_BOARD', 2, 280.00, 'Люкс'),
       (5, '303', 'LUXE', 'FULL_BOARD', 3, 330.00, 'Люкс семейный'),
       (5, '304', 'LUXE', 'FULL_BOARD', 2, 300.00, 'Люкс с террасой'),
       (5, '305', 'LUXE', 'ALL_INCLUSIVE', 4, 380.00, 'Люкс премиум'),
       (5, '401', 'SUITE', 'HALF_BOARD', 2, 350.00, 'Сьют'),
       (5, '402', 'SUITE', 'FULL_BOARD', 3, 400.00, 'Сьют семейный'),
       (5, '403', 'SUITE', 'ALL_INCLUSIVE', 4, 480.00, 'Сьют премиум'),
       (5, '404', 'SUITE', 'ALL_INCLUSIVE', 2, 420.00, 'Сьют с джакузи'),
       (5, 'V01', 'PRESIDENTIAL', 'ALL_INCLUSIVE', 4, 600.00, 'Вилла стандарт'),
       (5, 'V02', 'PRESIDENTIAL', 'ALL_INCLUSIVE', 6, 750.00, 'Вилла семейная'),
       (5, 'V03', 'PRESIDENTIAL', 'ALL_INCLUSIVE', 6, 800.00, 'Вилла с бассейном'),
       (5, 'V04', 'PRESIDENTIAL', 'ALL_INCLUSIVE', 8, 1000.00, 'Вилла президентская');

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
INSERT INTO guests (first_name, last_name, email, phone, created_at)
VALUES
    ('Осаму', 'Дадзай', 'osamu.dazai@mail.jp', '+81901112233', CURRENT_TIMESTAMP),
    ('Харуки', 'Мураками', 'haruki.murakami@mail.jp', '+81902223344', CURRENT_TIMESTAMP),
    ('Ясунари', 'Кавабата', 'yasunari.kawabata@mail.jp', '+81903334455', CURRENT_TIMESTAMP),
    ('Наоко', 'Кобаяси', 'naoko.kobayashi@mail.jp', '+81904445566', CURRENT_TIMESTAMP),
    ('Цукуру', 'Тадзаки', 'tsukuru.tazaki@mail.jp', '+81905556677', CURRENT_TIMESTAMP),
    ('Эминем', 'Эминемович', 'eminem@mail.com', '+19991234567', CURRENT_TIMESTAMP);

-- =====================================================
-- bookings
-- =====================================================
DO $$
DECLARE
    v_room_id BIGINT;
    v_guest_id BIGINT;
    v_booking_id BIGINT;
BEGIN
    -- Hotel 1: Hyatt
    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 1 AND room_number = '101';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'osamu.dazai@mail.jp';
    INSERT INTO bookings (room_id, guest_id, check_in_date, check_out_date, status, total_price, created_at, updated_at)
    VALUES (v_room_id, v_guest_id, '2026-03-01 14:00:00', '2026-03-05 12:00:00', 'CONFIRMED', 600.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 1 AND room_number = '201';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'haruki.murakami@mail.jp';
    INSERT INTO bookings (room_id, guest_id, check_in_date, check_out_date, status, total_price, created_at, updated_at)
    VALUES (v_room_id, v_guest_id, '2026-03-10 14:00:00', '2026-03-15 12:00:00', 'PAID', 1500.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 1 AND room_number = '303';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'yasunari.kawabata@mail.jp';
    INSERT INTO bookings (room_id, guest_id, check_in_date, check_out_date, status, total_price, created_at, updated_at)
    VALUES (v_room_id, v_guest_id, '2026-03-20 14:00:00', '2026-03-25 12:00:00', 'HOLD', 1600.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 1 AND room_number = '501';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'naoko.kobayashi@mail.jp';
    INSERT INTO bookings (room_id, guest_id, check_in_date, check_out_date, status, total_price, created_at, updated_at)
    VALUES (v_room_id, v_guest_id, '2026-04-01 14:00:00', '2026-04-05 12:00:00', 'CONFIRMED', 2000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 1 AND room_number = '603';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'tsukuru.tazaki@mail.jp';
    INSERT INTO bookings (room_id, guest_id, check_in_date, check_out_date, status, total_price, created_at, updated_at)
    VALUES (v_room_id, v_guest_id, '2026-04-10 14:00:00', '2026-04-15 12:00:00', 'PAID', 4500.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 1 AND room_number = '702';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'eminem@mail.com';
    INSERT INTO bookings (room_id, guest_id, check_in_date, check_out_date, status, total_price, created_at, updated_at)
    VALUES (v_room_id, v_guest_id, '2026-04-20 14:00:00', '2026-04-27 12:00:00', 'CONFIRMED', 8400.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
    RETURNING id INTO v_booking_id;
    INSERT INTO booking_additional_guests (booking_id, guest_id)
    SELECT v_booking_id, id FROM guests WHERE email = 'osamu.dazai@mail.jp';
    INSERT INTO booking_additional_guests (booking_id, guest_id)
    SELECT v_booking_id, id FROM guests WHERE email = 'haruki.murakami@mail.jp';

    -- Hotel 2: Silk Road Samarkand
    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 2 AND room_number = '301';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'osamu.dazai@mail.jp';
    INSERT INTO bookings (room_id, guest_id, check_in_date, check_out_date, status, total_price, created_at, updated_at)
    VALUES (v_room_id, v_guest_id, '2026-05-01 14:00:00', '2026-05-04 12:00:00', 'CONFIRMED', 480.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 2 AND room_number = '403';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'haruki.murakami@mail.jp';
    INSERT INTO bookings (room_id, guest_id, check_in_date, check_out_date, status, total_price, created_at, updated_at)
    VALUES (v_room_id, v_guest_id, '2026-05-10 14:00:00', '2026-05-15 12:00:00', 'HOLD', 1500.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 2 AND room_number = '503';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'yasunari.kawabata@mail.jp';
    INSERT INTO bookings (room_id, guest_id, check_in_date, check_out_date, status, total_price, created_at, updated_at)
    VALUES (v_room_id, v_guest_id, '2026-05-20 14:00:00', '2026-05-25 12:00:00', 'PAID', 2750.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    -- Hotel 3: Bukhara Palace
    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 3 AND room_number = '301';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'naoko.kobayashi@mail.jp';
    INSERT INTO bookings (room_id, guest_id, check_in_date, check_out_date, status, total_price, created_at, updated_at)
    VALUES (v_room_id, v_guest_id, '2026-06-01 14:00:00', '2026-06-05 12:00:00', 'CONFIRMED', 800.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 3 AND room_number = '401';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'tsukuru.tazaki@mail.jp';
    INSERT INTO bookings (room_id, guest_id, check_in_date, check_out_date, status, total_price, created_at, updated_at)
    VALUES (v_room_id, v_guest_id, '2026-06-10 14:00:00', '2026-06-14 12:00:00', 'HOLD', 1280.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    -- Hotel 4: Khiva Grand
    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 4 AND room_number = '304';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'eminem@mail.com';
    INSERT INTO bookings (room_id, guest_id, check_in_date, check_out_date, status, total_price, created_at, updated_at)
    VALUES (v_room_id, v_guest_id, '2026-06-20 14:00:00', '2026-06-25 12:00:00', 'CONFIRMED', 1400.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 4 AND room_number = '301';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'osamu.dazai@mail.jp';
    INSERT INTO bookings (room_id, guest_id, check_in_date, check_out_date, status, total_price, created_at, updated_at)
    VALUES (v_room_id, v_guest_id, '2026-07-01 14:00:00', '2026-07-05 12:00:00', 'PAID', 680.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    -- Hotel 5: Fergana Valley Resort
    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 5 AND room_number = '403';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'haruki.murakami@mail.jp';
    INSERT INTO bookings (room_id, guest_id, check_in_date, check_out_date, status, total_price, created_at, updated_at)
    VALUES (v_room_id, v_guest_id, '2026-07-10 14:00:00', '2026-07-15 12:00:00', 'CONFIRMED', 2400.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 5 AND room_number = 'V02';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'yasunari.kawabata@mail.jp';
    INSERT INTO bookings (room_id, guest_id, check_in_date, check_out_date, status, total_price, created_at, updated_at)
    VALUES (v_room_id, v_guest_id, '2026-07-20 14:00:00', '2026-07-27 12:00:00', 'PAID', 5250.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
    RETURNING id INTO v_booking_id;
    INSERT INTO booking_additional_guests (booking_id, guest_id)
    SELECT v_booking_id, id FROM guests WHERE email = 'naoko.kobayashi@mail.jp';
    INSERT INTO booking_additional_guests (booking_id, guest_id)
    SELECT v_booking_id, id FROM guests WHERE email = 'tsukuru.tazaki@mail.jp';

    SELECT id INTO v_room_id FROM rooms WHERE hotel_id = 5 AND room_number = 'V04';
    SELECT id INTO v_guest_id FROM guests WHERE email = 'tsukuru.tazaki@mail.jp';
    INSERT INTO bookings (room_id, guest_id, check_in_date, check_out_date, status, total_price, created_at, updated_at)
    VALUES (v_room_id, v_guest_id, '2026-08-01 14:00:00', '2026-08-10 12:00:00', 'HOLD', 9000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
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
