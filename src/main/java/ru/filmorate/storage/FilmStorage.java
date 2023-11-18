package ru.filmorate.storage;

import ru.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film create(Film film);

    void update(Film film);

    List<Film> getAll();

    Film getFilmById(Integer id);
}
