package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.execption.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final UserValidator validator;
    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    @Autowired
    public InMemoryUserStorage(UserValidator validator) {
        this.validator = validator;
    }

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
        } else throw new UserDoesNotExistException("Пользователь с id " + user.getId() + " не найден.");
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public User getUserById(Integer id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else throw new UserDoesNotExistException("Пользователь с id " + id + " не найден.");
    }

}
