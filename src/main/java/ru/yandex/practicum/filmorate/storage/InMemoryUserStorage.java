package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.execption.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    public User create(User user) {
        user.setId(nextId);
        users.put(nextId, user);
        nextId++;
        return user;
    }

    public void update(User user) {
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
