-- =====================================================
-- ТАБЛИЦЫ
-- =====================================================

-- cities
CREATE TABLE cities (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    country    VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- hotels
CREATE TABLE hotels (
    id                 BIGSERIAL PRIMARY KEY,
    city_id            BIGINT             NOT NULL REFERENCES cities(id),
    name               VARCHAR(255)       NOT NULL,
    description        VARCHAR(1000),
    stars              INTEGER CHECK (stars >= 1 AND stars <= 5),
    accommodation_type accommodation_type NOT NULL,
    created_at         TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at         TIMESTAMP
);

-- rooms
CREATE TABLE rooms (
    id              BIGSERIAL PRIMARY KEY,
    hotel_id        BIGINT         NOT NULL REFERENCES hotels(id),
    room_number     VARCHAR(50)    NOT NULL,
    room_type       room_type      NOT NULL,
    meal_plan       meal_plan      NOT NULL,
    capacity        INTEGER        NOT NULL,
    price_per_night DECIMAL(19,2)  NOT NULL,
    description     VARCHAR(500),
    created_at      TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at      TIMESTAMP
);

-- room_amenities
CREATE TABLE room_amenities (
    room_id BIGINT  NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
    amenity amenity NOT NULL,
    PRIMARY KEY (room_id, amenity)
);

-- room_price_history
CREATE TABLE room_price_history (
    id              BIGSERIAL PRIMARY KEY,
    room_id         BIGINT         NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
    price_per_night DECIMAL(19,2)  NOT NULL,
    valid_from      TIMESTAMP      NOT NULL,
    valid_to        TIMESTAMP,
    created_at      TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- guests
CREATE TABLE guests (
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    email      VARCHAR(255),
    phone      VARCHAR(50),
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- bookings
CREATE TABLE bookings (
    id             BIGSERIAL PRIMARY KEY,
    room_id        BIGINT         NOT NULL REFERENCES rooms(id),
    guest_id       BIGINT         NOT NULL REFERENCES guests(id),
    check_in_date  TIMESTAMP      NOT NULL,
    check_out_date TIMESTAMP      NOT NULL,
    status         booking_status NOT NULL,
    total_price    DECIMAL(19,2),
    created_at     TIMESTAMP      NOT NULL,
    updated_at     TIMESTAMP      NOT NULL,
    deleted_at     TIMESTAMP
);

-- booking_additional_guests
CREATE TABLE booking_additional_guests (
    booking_id BIGINT NOT NULL REFERENCES bookings(id) ON DELETE CASCADE,
    guest_id   BIGINT NOT NULL REFERENCES guests(id) ON DELETE CASCADE,
    PRIMARY KEY (booking_id, guest_id)
);

-- hotel_reviews
CREATE TABLE hotel_reviews (
    id          BIGSERIAL PRIMARY KEY,
    hotel_id    BIGINT    NOT NULL REFERENCES hotels(id) ON DELETE CASCADE,
    guest_id    BIGINT    NOT NULL REFERENCES guests(id),
    rating      INTEGER   NOT NULL CHECK (rating >= 1 AND rating <= 5),
    description VARCHAR(2000),
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- UNIQUE CONSTRAINTS
-- cities: имя + страна уникальны среди неудалённых
CREATE UNIQUE INDEX uk_cities_name_country ON cities(name, country) WHERE deleted_at IS NULL;

-- rooms: номер уникален в отеле среди неудалённых
CREATE UNIQUE INDEX uk_rooms_hotel_room_number ON rooms(hotel_id, room_number) WHERE deleted_at IS NULL;

-- guests: email уникален среди неудалённых
CREATE UNIQUE INDEX uk_guests_email ON guests(email) WHERE email IS NOT NULL AND deleted_at IS NULL;

-- hotel_reviews: один гость - один отзыв на отель
CREATE UNIQUE INDEX uk_hotel_reviews_hotel_guest ON hotel_reviews(hotel_id, guest_id);

