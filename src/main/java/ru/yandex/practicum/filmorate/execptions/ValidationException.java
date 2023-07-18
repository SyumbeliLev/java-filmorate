package ru.yandex.practicum.filmorate.execptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String s) {
        super(s);
    }
}
