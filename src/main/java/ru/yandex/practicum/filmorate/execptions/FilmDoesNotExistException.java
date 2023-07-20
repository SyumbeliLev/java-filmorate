package ru.yandex.practicum.filmorate.execptions;

public class FilmDoesNotExistException extends RuntimeException {
    public FilmDoesNotExistException(String s) {
        super(s);
    }
}