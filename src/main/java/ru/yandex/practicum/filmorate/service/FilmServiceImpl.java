package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.FilmDaoImpl;
import ru.yandex.practicum.filmorate.execption.IncorrectParameterException;
import ru.yandex.practicum.filmorate.execption.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FilmServiceImpl implements FilmService {
    private final FilmDaoImpl filmDao;

    @Override
    public void createFilm(Film film) {
        FilmValidator.check(film);
        filmDao.create(film);
    }

    @Override
    public void updateFilm(Film film) {
        FilmValidator.check(film);
        filmDao.update(film);
    }

    @Override
    public List<Film> getAllFilm() {
        return filmDao.getAll();
    }

    @Override
    public Film getFilmById(Integer id) {
        return filmDao.getFilmById(id);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        Film film = filmDao.getFilmById(filmId);
        film.getLikes().add(Long.valueOf(userId));

        //filmDao.addLike(filmIdm, userId);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        Film film = filmDao.getFilmById(filmId);
        if (film.getLikes().contains(Long.valueOf(userId))) {
            film.getLikes().remove(Long.valueOf(userId));
        } else {
            throw new UserDoesNotExistException("Пользователь с id: " + userId + " не найден");
        }

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
