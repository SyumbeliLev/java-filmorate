package ru.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.filmorate.model.User;
import ru.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final InMemoryUserStorage storage;
    private final UserDaoImpl userDao;

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
    public User getUserById(Integer id) {
        return userDao.getUserById(id);
    }

    @Override
    public void addToFriend(Integer userId, Integer friendId) {
        User user = storage.getUserById(userId);
        User friend = storage.getUserById(friendId);

        user.getFriends()
                .add(Long.valueOf(friendId));
        friend.getFriends()
                .add(Long.valueOf(userId));
    }

    @Override
    public void removeFriend(Integer userid, Integer friendId) {
        User user = storage.getUserById(userid);
        User friend = storage.getUserById(friendId);

        user.getFriends()
                .remove(Long.valueOf(friendId));
        friend.getFriends()
                .remove(Long.valueOf(userid));
    }

    @Override
    public List<User> getMutualFriends(Integer userId, Integer otherId) {

        Set<Long> user = storage.getUserById(userId)
                .getFriends();
        Set<Long> otherUser = storage.getUserById(otherId)
                .getFriends();

        return user.stream()
                .filter(otherUser::contains)
                .map(id -> storage.getUserById(Math.toIntExact(id)))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getListFriends(Integer userId) {
        User user = storage.getUserById(userId);
        return user.getFriends()
                .stream()
                .map(id -> storage.getUserById(Math.toIntExact(id)))
                .collect(Collectors.toList());
    }
}
