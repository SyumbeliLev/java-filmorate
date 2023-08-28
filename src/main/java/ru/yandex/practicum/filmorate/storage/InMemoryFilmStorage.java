package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.execption.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private int nextId = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    public Film create(Film film) {
        film.setId(nextId);
        films.put(nextId, film);
        nextId++;
        return film;
    }

    public void update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else throw new FilmDoesNotExistException("Фильм с id: " + film.getId() + " не найден.");
    }

    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    public Film getFilmById(Integer id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else throw new FilmDoesNotExistException("Фильм с id: " + id + " не найден.");
    }
}

