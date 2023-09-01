package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.execption.UserDoesNotExistException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
class FriendsDaoImpl implements FriendsDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addToFriend(Long userId, Long friendId) {
        boolean status = checkStatus(friendId, userId);
        if (status) {
            String sql = "UPDATE USER_FRIENDS SET status = ? WHERE user_id = ?";
            jdbcTemplate.update(sql, true, friendId);
        }
        String sqlQuery = "INSERT INTO USER_FRIENDS (user_id, friend_id,status) VALUES(?, ?,?);";
        checkExistUserId(userId);
        checkExistUserId(friendId);
        jdbcTemplate.update(sqlQuery, userId, friendId, status);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        String sqlQuery = "DELETE FROM USER_FRIENDS WHERE user_id = ? AND friend_id = ?;";
        checkExistUserId(userId);
        checkExistUserId(friendId);
        jdbcTemplate.update("UPDATE USER_FRIENDS SET status = ? WHERE user_id = ? AND friend_id = ?;", false, friendId, userId);
        if (jdbcTemplate.update(sqlQuery, userId, friendId) > 0) {
            log.info("Пользователь с id " + userId + " удалил пользователя с id " + friendId + " .");
        }
    }

    @Override
    public List<User> getMutualFriends(Long userId, Long otherId) {
        String sql = "SELECT * FROM USERS WHERE user_id in (SELECT us1.FRIEND_ID FROM USER_FRIENDS AS us1  JOIN USER_FRIENDS AS us2 ON us1.FRIEND_ID = us2.FRIEND_ID  WHERE us1.USER_ID = ? AND us2.USER_ID = ?);";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> buildUser(rs)), userId, otherId);
    }

    @Override
    public List<User> getListFriends(Long userId) {
        String sql = "SELECT * FROM USERS WHERE user_id in ( SELECT friend_id FROM USERS  INNER JOIN USER_FRIENDS UF on USERS.user_id = UF.user_id WHERE UF.user_id = ?);";
        return jdbcTemplate.query(sql, (rs, rowNum) -> buildUser(rs), userId);
    }

    private User buildUser(ResultSet rs) throws SQLException {
        return User.builder().id(rs.getLong("user_id")).email(rs.getString("email")).login(rs.getString("login")).name(rs.getString("name")).birthday(Objects.requireNonNull(rs.getTimestamp("birthday")).toLocalDateTime().toLocalDate()).build();
    }

    private void checkExistUserId(long userId) {
        SqlRowSet sqlUser = jdbcTemplate.queryForRowSet("SELECT user_id FROM users WHERE user_id = ?", userId);
        if (!sqlUser.next()) {
            log.info("Пользователь с идентификатором {} не найден.", userId);
            throw new UserDoesNotExistException(String.format("Пользователь с id: %d не найден", userId));
        }
    }

    private boolean checkStatus(Long userId, Long friendId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM USER_FRIENDS WHERE user_id = ? AND friend_id = ?", userId, friendId);
        return rs.next();
    }
}
