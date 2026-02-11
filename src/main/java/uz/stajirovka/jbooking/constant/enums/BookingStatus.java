package uz.stajirovka.jbooking.constant.enums;

public enum BookingStatus {
    HOLD,      // ожидание
    CONFIRMED, // подтверждено
    PAID,      // оплачено
    CANCELLED, // отменено
    MODIFIED; // изменено


    public static BookingStatus fromTransactionStatus(TransactionStatus transactionStatus) {
        if (transactionStatus == TransactionStatus.COMPLETED) {
            return BookingStatus.PAID;
        } else {
            return BookingStatus.HOLD;
        }
    }
}
