package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.List;

public interface GenreService {
    Genre getGenreById(Integer id);

    List<Genre> getAllGenre();
}

