package ru.yandex.practicum.filmorate.execption;

public class ValidationException extends RuntimeException {
    public ValidationException(String s) {
        super(s);
    }
}
