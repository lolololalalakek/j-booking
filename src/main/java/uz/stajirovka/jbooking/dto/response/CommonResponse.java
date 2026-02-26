package uz.stajirovka.jbooking.dto.response;

public record CommonResponse<T>(T data) {
    public static <T> CommonResponse<T> of(T data) {
        return new CommonResponse<>(data);
    }
}
