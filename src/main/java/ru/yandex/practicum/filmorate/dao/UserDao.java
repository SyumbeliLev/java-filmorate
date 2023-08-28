package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDao {

    void create(User user);

    void update(User user);

    List<User> getAll();

    User getUserById(Integer id);
}
