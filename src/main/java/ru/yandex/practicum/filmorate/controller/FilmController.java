package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;


import java.util.List;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService service = new FilmService();

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        service.create(film);
        log.info("Добавление фильм:" + film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        service.update(film);
        log.info("Обновление фильма с id: {}", film.getId());
        return film;
    }

    @GetMapping
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", service.getAll().size());
        return service.getAll();
    }
}
