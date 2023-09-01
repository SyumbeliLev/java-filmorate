package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.execption.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class FilmServiceImpl implements FilmService {
    private final FilmDao filmDao;
    private final FilmLikeDao filmLikeDao;

    @Override
    @Transactional
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
        if (count <= 0) {
            throw new IncorrectParameterException(count.toString());
        }
        return filmDao.getAll().stream().sorted((p0, p1) -> {
            int likesValue1 = p0.getLikes().size();
            int likesValue2 = p1.getLikes().size();
            return Integer.compare(likesValue2, likesValue1);
        }).limit(count).collect(Collectors.toList());
    }
}
