package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.execption.UserDoesNotExistException;

import java.util.*;

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
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT *\n" +
                "FROM USERS\n" +
                "WHERE user_id in (SELECT us1.FRIEND_ID\n" +
                "                  FROM USER_FRIENDS AS us1\n" +
                "                           JOIN USER_FRIENDS AS us2 ON us1.FRIEND_ID = us2.FRIEND_ID\n" +
                "                  WHERE us1.USER_ID = ?\n" +
                "                    AND us2.USER_ID = ?);", userId, otherId);
        List<User> friends = new ArrayList<>();
        while (rs.next()) {
            friends.add(buildUser(rs));
        }
        log.info("Общих друзей в списке у пользователей {} и {} : {}", userId, otherId, friends.size());
        return friends;
    }

    @Override
    public List<User> getListFriends(Long userId) {
        SqlRowSet rs = jdbcTemplate
                .queryForRowSet("SELECT * FROM USERS WHERE user_id in ( SELECT friend_id FROM USERS  INNER JOIN USER_FRIENDS UF on USERS.user_id = UF.user_id WHERE UF.user_id = ?);",
                        userId);
        List<User> friends = new ArrayList<>();
        while (rs.next()) {
            friends.add(buildUser(rs));
        }
        return friends;
    }

    private User buildUser(SqlRowSet rs) {
        return User.builder()
                .id(rs.getLong("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(Objects.requireNonNull(rs.getTimestamp("birthday")).toLocalDateTime().toLocalDate())
                .build();
    }

    private void checkExistUserId(long userId) {
        String query = "SELECT COUNT(*)  FROM USERS WHERE user_id = ?";
        int count = jdbcTemplate.queryForObject(query, new Object[]{userId}, Integer.class);
        if (count < 1) {
            throw new UserDoesNotExistException("Пользователь с id " + userId + " не найден.");
        }
    }

    private boolean checkStatus(Long userId, Long friendId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM USER_FRIENDS WHERE user_id = ? AND friend_id = ?", userId, friendId);
        return rs.next();
    }
}
