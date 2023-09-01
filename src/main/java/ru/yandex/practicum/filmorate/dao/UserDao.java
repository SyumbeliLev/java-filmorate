package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDao {

    User create(User user);

    User update(User user);

    List<User> getAll();

    User getUserById(Long id);
}
