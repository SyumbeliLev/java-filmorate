package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    void createUser(User user);

    void updateUser(User user);

    List<User> getAllFilm();

    User getUserById(Integer id);

    void addToFriend(Integer userId, Integer friendId);

    void removeFriend(Integer userid, Integer friendId);

    List<User> getMutualFriends(Integer userId, Integer otherId);

    List<User> getListFriends(Integer userId);
}
