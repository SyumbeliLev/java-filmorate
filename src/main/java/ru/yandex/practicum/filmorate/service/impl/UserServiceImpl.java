package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final FriendsDao friendsDao;

    @Override
    public void createUser(User user) {
        UserValidator.check(user);
        userDao.create(user);
    }

    @Override
    public void updateUser(User user) {
        UserValidator.check(user);
        userDao.update(user);
    }

    @Override
    public List<User> getALlUser() {
        return userDao.getAll();
    }

    @Override
    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    public void addToFriend(Long userId, Long friendId) {
        friendsDao.addToFriend(userId, friendId);
    }

    @Override
    public void removeFriend(Long userid, Long friendId) {
        friendsDao.removeFriend(userid, friendId);
    }

    @Override
    public List<User> getMutualFriends(Long userId, Long otherId) {
       return friendsDao.getMutualFriends(userId, otherId);
    }

    @Override
    public List<User> getListFriends(Long userId) {
        return friendsDao.getListFriends(userId);
    }
}
