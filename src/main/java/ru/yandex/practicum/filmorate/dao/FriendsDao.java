package ru.yandex.practicum.filmorate.dao;


import ru.yandex.practicum.filmorate.entity.User;

import java.util.List;

public interface FriendsDao {
    void addToFriend(Long userId, Long friendId);

    void removeFriend(Long userid, Long friendId);

    List<User> getMutualFriends(Long userId, Long otherId);

    List<User> getListFriends(Long userId);
}
