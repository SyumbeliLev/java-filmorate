package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.execption.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public void create(User user) {
        String sql = "INSERT INTO PUBLIC.USER (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE PUBLIC.USER SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?";
        int userId = user.getId();
        int update = jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), userId);
        if (update == 1) {
            log.info("Личные данные пользователя с id " + user.getId() + " обновлены");
        } else {
            throw new UserDoesNotExistException("Пользователь с id " + user.getId() + " не найден.");
        }
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM PUBLIC.USER";
        return jdbcTemplate.query(sql, (rs, rowNum) -> User.builder()
                .id(rs.getInt("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getTimestamp("birthday").toLocalDateTime().toLocalDate())
                .build());
    }

    @Override
    public User getUserById(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM PUBLIC.USER WHERE USER_ID = ?", id);

        if (userRows.next()) {
            return User.builder()
                    .id(userRows.getInt("user_id"))
                    .email(userRows.getString("email"))
                    .login(userRows.getString("login"))
                    .name(userRows.getString("name"))
                    .birthday(Objects.requireNonNull(userRows.getTimestamp("birthday")).toLocalDateTime().toLocalDate())
                    .build();
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new UserDoesNotExistException("Пользователь с id " + id + " не найден.");
        }
    }
}
