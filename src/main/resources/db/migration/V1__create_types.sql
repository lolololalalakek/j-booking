-- ENUM ТИПЫ
CREATE TYPE accommodation_type AS ENUM (
    'HOTEL',
    'APARTMENT',
    'HOSTEL',
    'HOUSE',
    'VILLA'
);

CREATE TYPE room_type AS ENUM (
    'STANDARD',
    'SEMI_LUXE',
    'LUXE',
    'SUITE',
    'PRESIDENTIAL'
);

CREATE TYPE meal_plan AS ENUM (
    'ROOM_ONLY',
    'BREAKFAST',
    'HALF_BOARD',
    'FULL_BOARD',
    'ALL_INCLUSIVE'
);

CREATE TYPE amenity AS ENUM (
    'WIFI',
    'AIR_CONDITIONING',
    'TV',
    'MINI_BAR',
    'SAFE'
);

CREATE TYPE booking_status AS ENUM (
    'HOLD',
    'CONFIRMED',
    'PAID',
    'CANCELLED',
    'MODIFIED'
);