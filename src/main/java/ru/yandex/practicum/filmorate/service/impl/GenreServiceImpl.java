package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Service
@RequiredArgsConstructor
class GenreServiceImpl implements GenreService {
    private final GenreDao genreDao;

    @Override
    public Genre getGenreById(Integer id) {
        return genreDao.getGenreById(id);
    }

    @Override
    public List<Genre> getAllGenre() {
        return genreDao.getAllGenre();
    }
}
