CREATE INDEX idx_bookings_status_updated_at
    ON bookings(status, updated_at)
    WHERE status = 'PAYMENT_PROCESSING';
