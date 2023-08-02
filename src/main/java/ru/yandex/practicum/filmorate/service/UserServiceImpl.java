package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    private final InMemoryUserStorage storage;

    @Autowired
    public UserServiceImpl(InMemoryUserStorage storage) {
        this.storage = storage;
    }

    public void createUser(User user){
        UserValidator.check(user);
        storage.create(user);
    }

    public void updateUser(User user) {
        UserValidator.check(user);
        storage.update(user);
    }

    public List<User> getAllFilm(){
        return storage.getAll();
    }

    public User getUserById(Integer id){
        return storage.getUserById(id);
    }

    public void addToFriend(Integer userId, Integer friendId) {
        User user = storage.getUserById(userId);
        User friend = storage.getUserById(friendId);

        user.getFriends().add(Long.valueOf(friendId));
        friend.getFriends().add(Long.valueOf(userId));
    }

    public void removeFriend(Integer userid, Integer friendId) {
        User user = storage.getUserById(userid);
        User friend = storage.getUserById(friendId);

        user.getFriends().remove(Long.valueOf(friendId));
        friend.getFriends().remove(Long.valueOf(userid));
    }

    public List<User> getMutualFriends(Integer userId, Integer otherId) {
        Set<Long> user = storage.getUserById(userId).getFriends();
        Set<Long> otherUser = storage.getUserById(otherId).getFriends();
        return user.stream().filter(otherUser::contains).map(id -> storage.getUserById(Math.toIntExact(id))).collect(Collectors.toList());
    }

    public List<User> getListFriends(Integer userId) {
        User user = storage.getUserById(userId);
        return user.getFriends().stream().map(id -> storage.getUserById(Math.toIntExact(id))).collect(Collectors.toList());
    }
}
