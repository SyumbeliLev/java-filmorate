package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    void createUser(User user);

    void updateUser(User user);

    List<User> getALlUser();

    User getUserById(Long id);

    void addToFriend(Long userId, Long friendId);

    void removeFriend(Long userid, Long friendId);

    List<User> getMutualFriends(Long userId, Long otherId);

    List<User> getListFriends(Long userId);
}
