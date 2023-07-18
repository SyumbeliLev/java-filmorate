package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.execptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    int nextId = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public ResponseEntity<Film> create(@Validated @RequestBody Film film) {
        Film validFilm = checkValidation(film);
        validFilm.setId(nextId);
        films.put(nextId, validFilm);
        log.info("Добавление фильм:" + validFilm);
        nextId++;
        return new ResponseEntity<>(validFilm, HttpStatusCode.valueOf(200));
    }

    @PutMapping
    public ResponseEntity<Film> update(@RequestBody Film film) {
        Film validFilm = checkValidation(film);
        log.debug("Обновлён фильм :" + validFilm);
        if (films.containsKey(validFilm.getId())) {
            films.put(validFilm.getId(), validFilm);
            return new ResponseEntity<>(validFilm, HttpStatusCode.valueOf(HttpStatus.SC_OK));
        } else {
            return new ResponseEntity<>(validFilm, HttpStatusCode.valueOf(HttpStatus.SC_NOT_FOUND));
        }
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return new ResponseEntity<>(films.values(), HttpStatusCode.valueOf(200));
    }


    private Film checkValidation(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("название не может быть пустым.");

        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("максимальная длина описания — 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("продолжительность фильма должна быть положительной.");
        }
        return film;
    }
}
