package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.execption.UserDoesNotExistException;

@Slf4j
@Component
@RequiredArgsConstructor
class FilmLikeDaoImpl implements FilmLikeDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(Long filmId, Long userId) {
        SqlRowSet sql = jdbcTemplate.queryForRowSet("SELECT * FROM FILM_LIKES WHERE film_id = ? AND user_id = ?", filmId, userId);
        checkExistUserId(userId);
        if (sql.next()) {
            throw new RuntimeException("Вы уже ставили лайк этому фильму");
        } else {
            String sqlQueryAddFriend = "INSERT INTO FILM_LIKES (film_id, user_id) VALUES (?, ?)";
            jdbcTemplate.update(sqlQueryAddFriend, filmId, userId);
            log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
        }
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        SqlRowSet sql = jdbcTemplate.queryForRowSet("SELECT * FROM FILM_LIKES WHERE film_id = ? AND user_id = ?", filmId, userId);
        checkExistUserId(userId);
        if (!sql.next()) {
            throw new RuntimeException("Вы не ставили лайк этому фильму");
        } else {
            String sqlQueryAddFriend = "DELETE FROM FILM_LIKES WHERE film_id = ? AND user_id = ?";
            jdbcTemplate.update(sqlQueryAddFriend, filmId, userId);
            log.info("Пользователь {} удалил лайк у фильма {}", userId, filmId);
        }
    }

    private void checkExistUserId(long userId) {
        SqlRowSet sqlUser = jdbcTemplate.queryForRowSet("SELECT user_id FROM users WHERE user_id = ?", userId);
        if (!sqlUser.next()) {
            log.info("Пользователь с идентификатором {} не найден.", userId);
            throw new UserDoesNotExistException(String.format("Пользователь с id: %d не найден", userId));
        }
    }
}
