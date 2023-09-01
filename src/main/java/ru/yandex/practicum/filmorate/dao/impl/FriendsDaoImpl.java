package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.execption.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
class FriendsDaoImpl implements FriendsDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;

    @Override
    public void addToFriend(Long userId, Long friendId) {
        boolean status = checkStatus(friendId, userId);

        if (status) {
            String sql = "UPDATE USER_FRIENDS SET STATUS = ? WHERE USER_ID = ?";
            jdbcTemplate.update(sql, true, friendId);
        }
        String sqlQuery = "INSERT INTO USER_FRIENDS (USER_ID, FRIEND_ID,STATUS) VALUES(?, ?,?);";

        checkExistUserId(userId);
        checkExistUserId(friendId);
        jdbcTemplate.update(sqlQuery, userId, friendId, status);
    }

    @Override
    public void removeFriend(Long userid, Long friendId) {
        String sqlQuery = "DELETE FROM USER_FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?;";

        jdbcTemplate.update("UPDATE USER_FRIENDS SET STATUS = ? WHERE USER_ID = ? AND FRIEND_ID = ?;", false, friendId, userid);

        if (jdbcTemplate.update(sqlQuery, userid, friendId) > 0) {
            log.info("Пользователь с id " + userid + " удалил пользователя с id " + friendId + " .");
        }
    }

    @Override
    public List<User> getMutualFriends(Long userId, Long otherId) {
        checkExistUserId(userId);
        checkExistUserId(otherId);

        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT FRIEND_ID  FROM USER_FRIENDS  WHERE USER_ID = ? AND  FRIEND_ID IN( SELECT FRIEND_ID   FROM USER_FRIENDS  WHERE USER_ID = ?);", userId, otherId);
        List<User> listMutualFriends = new ArrayList<>();
        while (rs.next()) {
            User mutualFriend = userDao.getUserById(rs.getLong("FRIEND_ID"));
            listMutualFriends.add(mutualFriend);
        }
        return listMutualFriends;
    }

    @Override
    public List<User> getListFriends(Long userId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT FRIEND_ID FROM USER_FRIENDS WHERE USER_ID = ?", userId);
        Set<Long> friendsIdSet = new HashSet<>();
        while (rs.next()) {
            friendsIdSet.add(rs.getLong("FRIEND_ID"));
        }
        List<User> friends = new ArrayList<>();
        for (Long idFriends : friendsIdSet) {
            friends.add(userDao.getUserById(idFriends));
        }
        return friends;
    }

    private void checkExistUserId(long userId) {
        String query = "SELECT COUNT(*)  FROM USER WHERE USER_ID = ?";
        int count = jdbcTemplate.queryForObject(query, new Object[]{userId}, Integer.class);
        if (count < 1) {
            throw new UserDoesNotExistException("Пользователь с id " + userId + " не найден.");
        }
    }

    private boolean checkStatus(Long userId, Long friendId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM USER_FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?", userId, friendId);
        return rs.next();
    }
}
