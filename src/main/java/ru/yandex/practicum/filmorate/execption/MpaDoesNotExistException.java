package ru.yandex.practicum.filmorate.execption;

public class MpaDoesNotExistException extends RuntimeException {
    public MpaDoesNotExistException(String s) {
        super(s);
    }
}
