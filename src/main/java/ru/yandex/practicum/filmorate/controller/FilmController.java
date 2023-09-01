package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        service.createFilm(film);
        log.info("Добавление фильм:" + film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Обновление фильма с id: {}", film.getId());
        return service.updateFilm(film);
    }

    @GetMapping
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", service.getAllFilm().size());
        return service.getAllFilm();
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable Long id) {
        return service.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Integer addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.debug("Текущее количество лайков фильма: {}", service.getFilmById(id).getLikes().size());
        service.addLike(id, userId);
        return service.getFilmById(id).getLikes().size();
    }


    @DeleteMapping("/{id}/like/{userId}")
    public Integer deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.debug("Текущее количество лайков фильма: {}", service.getFilmById(id).getLikes().size());
        service.removeLike(id, userId);
        return service.getFilmById(id).getLikes().size();
    }

    @GetMapping("/popular")
    public List<Film> findCountPopularFilms(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        return service.getPopularFilms(count);
    }
}

