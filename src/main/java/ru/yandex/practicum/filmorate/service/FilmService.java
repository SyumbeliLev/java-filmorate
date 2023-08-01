package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

 public interface FilmService {
     void createFilm(Film film);

     void updateFilm(Film film);

     List<Film> getAllFilm();

     Film getFilmById(Integer id);

    void addLike(Integer filmId, Integer userId);

     void removeLike(Integer filmId, Integer userId);

     List<Film> getPopularFilms(Integer count);

}
