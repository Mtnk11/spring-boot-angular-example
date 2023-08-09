package kz.madina.сustomer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
) {
}
