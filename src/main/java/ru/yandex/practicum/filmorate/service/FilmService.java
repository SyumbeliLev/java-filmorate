package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.execptions.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.*;

public class FilmService {
    private final FilmValidator validator = new FilmValidator();
    private int nextId = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    public Film create(Film film) {
        validator.check(film);
        film.setId(nextId);
        films.put(nextId, film);
        nextId++;
        return film;
    }

    public void update(Film film) {
        validator.check(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else throw new FilmDoesNotExistException("Фильм с таким id не найден.");
    }

    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }
}
