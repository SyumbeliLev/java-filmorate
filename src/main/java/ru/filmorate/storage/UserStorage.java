package ru.filmorate.storage;

import ru.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User create(User user);

    void update(User user);

    List<User> getAll();

    User getUserById(Integer id);
}
