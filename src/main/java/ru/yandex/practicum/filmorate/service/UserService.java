package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.execptions.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.*;

public class UserService {
    private final UserValidator validator = new UserValidator();
    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    public User create(User user) {
        validator.check(user);
        user.setId(nextId);
        users.put(nextId, user);
        nextId++;
        return user;
    }

    public void update(User user) {
        validator.check(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else throw new UserDoesNotExistException("Пользователь с таким id не найден.");
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }
}
