package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.entity.Film;

import java.util.List;

public interface FilmDao {
    Film create(Film film);

    Film update(Film film);

    List<Film> getAllFilm();

    Film getFilmById(Long id);

    List<Film> getPopularFilms(Integer limit);
}
