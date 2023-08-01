package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        service.createFilm(film);
        log.info("Добавление фильм:" + film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        service.updateFilm(film);
        log.info("Обновление фильма с id: {}", film.getId());
        return film;
    }

    @GetMapping
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", service.getAllFilm().size());
        return service.getAllFilm();
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable Integer id) {
        return service.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Integer addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.debug("Текущее количество лайков фильма: {}", service.getFilmById(id).getLikes().size());
        service.addLike(id, userId);
        return service.getFilmById(id).getLikes().size();
    }


    @DeleteMapping("/{id}/like/{userId}")
    public Integer deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.debug("Текущее количество лайков фильма: {}", service.getFilmById(id).getLikes().size());
        service.removeLike(id, userId);
        return service.getFilmById(id).getLikes().size();
    }

    @GetMapping("/popular")
    public List<Film> findCountPopularFilms(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        return service.getPopularFilms(count);
    }
}

