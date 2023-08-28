package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDao {
    void create(Film film);

    void update(Film film);

    List<Film> getAll();

    Film getFilmById(Integer id);
}
