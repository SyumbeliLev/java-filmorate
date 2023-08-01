package ru.yandex.practicum.filmorate.execption;

public class FilmDoesNotExistException extends RuntimeException {
    public FilmDoesNotExistException(String s) {
        super(s);
    }
}