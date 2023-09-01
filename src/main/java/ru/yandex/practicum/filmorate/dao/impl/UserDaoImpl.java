package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.execption.UserDoesNotExistException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("user_id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE USERS SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
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
        String sqlFilm = "SELECT * FROM USERS";
        return jdbcTemplate.query(sqlFilm, (rs, rowNum) -> resultSetToUser(rs));
    }

    @Override
    public User getUserById(Long userId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE user_id = ?", userId);
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
                .build();
    }

    private User resultSetToUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(Objects.requireNonNull(rs.getTimestamp("birthday")).toLocalDateTime().toLocalDate())
                .build();
    }
}
