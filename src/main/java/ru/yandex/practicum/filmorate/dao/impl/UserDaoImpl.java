package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.execption.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USER")
                .usingGeneratedKeyColumns("USER_ID");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE PUBLIC.USER SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?";
        Long userId = user.getId();
        int update = jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), userId);
        if (update == 1) {
            log.info("Личные данные пользователя с id " + userId + " обновлены");
        } else {
            throw new UserDoesNotExistException("Пользователь с id " + userId + " не найден.");
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM PUBLIC.USER");
        List<User> userList = new ArrayList<>();
        while (rs.next()) {
            userList.add(rowSetToUser(rs));
        }
        return userList;
    }

    @Override
    public User getUserById(Long userId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM PUBLIC.USER WHERE USER_ID = ?", userId);
        if (rs.next()) {
            return rowSetToUser(rs);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", userId);
            throw new UserDoesNotExistException("Пользователь с id " + userId + " не найден.");
        }
    }

    private User rowSetToUser(SqlRowSet rs) {
        return User.builder()
                .id(rs.getLong("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(Objects.requireNonNull(rs.getTimestamp("birthday")).toLocalDateTime().toLocalDate())
                .friends(getSetFriendsId(rs.getLong("user_id")))
                .build();
    }

    public Set<Long> getSetFriendsId(Long userId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM USER_FRIENDS WHERE USER_ID = ?", userId);
        Set<Long> friendsIdSet = new HashSet<>();
        while (rs.next()) {
            friendsIdSet.add(rs.getLong("FRIEND_ID"));
        }
        return friendsIdSet;
    }

}
