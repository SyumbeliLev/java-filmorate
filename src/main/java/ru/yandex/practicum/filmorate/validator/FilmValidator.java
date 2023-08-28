package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.execption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashSet;

@Slf4j
public class FilmValidator {
    public static void check(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Не валидное название");
            throw new ValidationException("Название не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Не валидное описание");
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Не валидная дата релиза");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() < 0) {
            log.warn("Не валидная продолжительность фильма");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
    }
}
