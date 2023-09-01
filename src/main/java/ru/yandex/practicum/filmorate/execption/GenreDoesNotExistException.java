package ru.yandex.practicum.filmorate.execption;

public class GenreDoesNotExistException extends RuntimeException {
    public GenreDoesNotExistException(String s) {
        super(s);
    }
}
