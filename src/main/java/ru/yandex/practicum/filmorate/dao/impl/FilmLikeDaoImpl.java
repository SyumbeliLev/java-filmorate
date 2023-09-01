package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
@Component
@RequiredArgsConstructor
class FilmLikeDaoImpl implements FilmLikeDao {
    private final UserDao userDao;
    private final FilmDao filmDao;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(Long filmId, Long userId) {
        User user = userDao.getUserById(userId);
        Film film = filmDao.getFilmById(filmId);
        String sqlQuery = "INSERT INTO FILM_LIKE (FILM_ID, USER_ID) VALUES(?, ?);";
        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        User user = userDao.getUserById(userId);
        Film film = filmDao.getFilmById(filmId);
        String sqlQuery = "DELETE FROM FILM_LIKE WHERE FILM_ID = ? AND USER_ID = ?;";
        if (jdbcTemplate.update(sqlQuery, film.getId(), user.getId()) > 0) {
            log.info("Пользовать с id " + user.getId() + " удалил лайк у фильма с  id " + film.getId() + " .");
        }
    }
}
