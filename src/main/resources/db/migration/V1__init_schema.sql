CREATE TABLE cities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL
);

CREATE TABLE hotels (
    id BIGSERIAL PRIMARY KEY,
    city_id BIGINT NOT NULL REFERENCES cities(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    stars DOUBLE PRECISION,
    accommodation_type VARCHAR(50) NOT NULL,
    brand VARCHAR(255)
);

CREATE TABLE rooms (
    id BIGSERIAL PRIMARY KEY,
    hotel_id BIGINT NOT NULL REFERENCES hotels(id),
    room_number VARCHAR(50) NOT NULL,
    capacity INT NOT NULL,
    price_per_night DECIMAL(10, 2) NOT NULL,
    description TEXT
);

CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    room_id BIGINT NOT NULL REFERENCES rooms(id),
    guest_first_name VARCHAR(255) NOT NULL,
    guest_last_name VARCHAR(255) NOT NULL,
    guest_email VARCHAR(255),
    number_of_guests INT NOT NULL,
    check_in_date TIMESTAMP NOT NULL,
    check_out_date TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    total_price DECIMAL(10, 2),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);
