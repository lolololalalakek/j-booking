package uz.stajirovka.jbooking.constant.enums;

public enum BookingStatus {
    HOLD,                // ожидание оплаты
    PAYMENT_PROCESSING,  // платёж инициирован, ждём результат
    CONFIRMED,           // оплачено и подтверждено
    MODIFIED,            // изменено после подтверждения
    CANCELLED;           // отменено

    // заглушка: CREATED трактуем как успех, пока TransactionProcessing не дописан
    // TODO: заменить на COMPLETED когда платёжка будет полностью готова
    public static boolean isPaymentSuccessful(TransactionStatus transactionStatus) {
        return transactionStatus == TransactionStatus.CREATED;
    }
}
