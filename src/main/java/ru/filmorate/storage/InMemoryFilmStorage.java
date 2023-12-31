package ru.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.filmorate.model.Film;
import ru.yandex.practicum.filmorate.execption.FilmDoesNotExistException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private int nextId = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        film.setId(nextId);
        films.put(nextId, film);
        nextId++;
        return film;
    }

    @Override
    public void update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else throw new FilmDoesNotExistException("Фильм с id: " + film.getId() + " не найден.");
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Integer id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else throw new FilmDoesNotExistException("Фильм с id: " + id + " не найден.");
    }
}

