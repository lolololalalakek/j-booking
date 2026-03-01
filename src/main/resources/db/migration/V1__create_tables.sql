-- =====================================================
-- TABLES
-- =====================================================

-- cities
CREATE TABLE cities
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    country    VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- hotels
CREATE TABLE hotels
(
    id                 BIGSERIAL PRIMARY KEY,
    city_id            BIGINT       NOT NULL REFERENCES cities (id),
    name               VARCHAR(255) NOT NULL,
    description        VARCHAR(1000),
    stars              INTEGER CHECK (stars >= 1 AND stars <= 5),
    accommodation_type VARCHAR(50)  NOT NULL,
    created_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at         TIMESTAMP,
    CONSTRAINT hotels_accommodation_type_chk
        CHECK (accommodation_type = ANY (ARRAY ['HOTEL', 'APARTMENT', 'HOSTEL', 'HOUSE', 'VILLA']::VARCHAR[]))
);

-- rooms
CREATE TABLE rooms
(
    id              BIGSERIAL PRIMARY KEY,
    hotel_id        BIGINT      NOT NULL REFERENCES hotels (id),
    room_number     VARCHAR(50) NOT NULL,
    room_type       VARCHAR(50) NOT NULL,
    meal_plan       VARCHAR(50) NOT NULL,
    capacity        INTEGER     NOT NULL,
    price_per_night BIGINT      NOT NULL,
    description     VARCHAR(500),
    created_at      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at      TIMESTAMP,
    CONSTRAINT rooms_room_type_chk
        CHECK (room_type = ANY (ARRAY ['STANDARD', 'SEMI_LUXE', 'LUXE', 'SUITE', 'PRESIDENTIAL']::VARCHAR[])),
    CONSTRAINT rooms_meal_plan_chk
        CHECK (meal_plan = ANY (ARRAY ['ROOM_ONLY', 'BREAKFAST', 'HALF_BOARD', 'FULL_BOARD', 'ALL_INCLUSIVE']::VARCHAR[]))
);

-- room_amenities
CREATE TABLE room_amenities
(
    room_id BIGINT      NOT NULL REFERENCES rooms (id) ON DELETE CASCADE,
    amenity VARCHAR(50) NOT NULL,
    PRIMARY KEY (room_id, amenity),
    CONSTRAINT room_amenities_amenity_chk
        CHECK (amenity = ANY (ARRAY ['WIFI', 'AIR_CONDITIONING', 'TV', 'MINI_BAR', 'SAFE']::VARCHAR[]))
);

-- room_price_history
CREATE TABLE room_price_history
(
    id              BIGSERIAL PRIMARY KEY,
    room_id         BIGINT    NOT NULL REFERENCES rooms (id) ON DELETE CASCADE,
    price_per_night BIGINT    NOT NULL,
    valid_from      TIMESTAMP NOT NULL,
    valid_to        TIMESTAMP,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- guests
CREATE TABLE guests
(
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    pinfl      VARCHAR(14)  NOT NULL,
    email      VARCHAR(255),
    phone      VARCHAR(50),
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- bookings
CREATE TABLE bookings
(
    id              BIGSERIAL PRIMARY KEY,
    city_id         BIGINT      NOT NULL REFERENCES cities (id),
    hotel_id        BIGINT      NOT NULL REFERENCES hotels (id),
    room_id         BIGINT      NOT NULL REFERENCES rooms (id),
    main_guest_id   BIGINT      NOT NULL REFERENCES guests (id),
    check_in_date   TIMESTAMP   NOT NULL,
    check_out_date  TIMESTAMP   NOT NULL,
    status          VARCHAR(50) NOT NULL,
    price_per_night BIGINT      NOT NULL,
    total_price     BIGINT,
    payment_id      BIGINT,
    notification_id BIGINT,
    total_guests    INT         NOT NULL,
    created_at      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at      TIMESTAMP,
    CONSTRAINT bookings_status_chk
        CHECK (status = ANY (ARRAY ['HOLD', 'CONFIRMED', 'PAID', 'CANCELLED']::VARCHAR[]))
);

-- booking_additional_guests
CREATE TABLE booking_additional_guests
(
    booking_id BIGINT NOT NULL REFERENCES bookings (id) ON DELETE CASCADE,
    guest_id   BIGINT NOT NULL REFERENCES guests (id) ON DELETE CASCADE,
    PRIMARY KEY (booking_id, guest_id)
);

-- payment_transactions (payment audit log)
CREATE TABLE payment_transactions
(
    id                     BIGSERIAL PRIMARY KEY,
    transaction_id         BIGINT      NOT NULL,
    booking_id             BIGINT      NOT NULL REFERENCES bookings (id),
    reference_id           UUID      NOT NULL,
    status                 VARCHAR(50) NOT NULL,
    amount                 BIGINT      NOT NULL,
    currency               VARCHAR(10) NOT NULL,
    transaction_created_at TIMESTAMP,
    created_at             TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- hotel_reviews
CREATE TABLE hotel_reviews
(
    id          BIGSERIAL PRIMARY KEY,
    hotel_id    BIGINT    NOT NULL REFERENCES hotels (id) ON DELETE CASCADE,
    guest_id    BIGINT    NOT NULL REFERENCES guests (id),
    rating      INTEGER   NOT NULL CHECK (rating >= 1 AND rating <= 5),
    description VARCHAR(2000),
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);