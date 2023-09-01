package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.List;

public interface GenreDao {
    Genre getGenreById(Integer id);

    List<Genre> getAllGenre();
}
