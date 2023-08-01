package ru.yandex.practicum.filmorate.execption;

public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException(String s) {
        super(s);
    }
}
