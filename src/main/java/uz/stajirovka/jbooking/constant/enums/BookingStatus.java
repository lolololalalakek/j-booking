package uz.stajirovka.jbooking.constant.enums;

public enum BookingStatus {
    HOLD,                // ожидание оплаты
    PAYMENT_PROCESSING,  // платёж инициирован, ждём результат
    CONFIRMED,           // оплачено и подтверждено
    MODIFIED,            // изменено после подтверждения
    CANCELLED;           // отменено

    public static boolean isPaymentSuccessful(TransactionStatus transactionStatus) {
        return transactionStatus == TransactionStatus.COMPLETED;
    }
}
