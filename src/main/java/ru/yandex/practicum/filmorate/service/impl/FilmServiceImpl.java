package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
class FilmServiceImpl implements FilmService {
    private final FilmDao filmDao;
    private final FilmLikeDao filmLikeDao;

    @Override
    public void createFilm(Film film) {
        FilmValidator.check(film);
        filmDao.create(film);
    }

    @Override
    public Film updateFilm(Film film) {
        FilmValidator.check(film);
        return filmDao.update(film);
    }

    @Override
    public List<Film> getAllFilm() {
        return filmDao.getAll();
    }

    @Override
    public Film getFilmById(Long id) {
        return filmDao.getFilmById(id);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        filmLikeDao.addLike(filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        filmLikeDao.removeLike(filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
      return filmDao.getPopularFilms(count);
    }
}
