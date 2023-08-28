package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        service.createUser(user);
        log.info("Добавление пользователя:" + user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        service.updateUser(user);
        log.info("Обновление пользователя с id: {}", user.getId());
        return user;
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable Integer id) {
        return service.getUserById(id);
    }

    @GetMapping
    public List<User> findAll() {
        log.debug("Текущее количество пользователей: {}", service.getAllFilm().size());
        return service.getAllFilm();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public String addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        service.addToFriend(id, friendId);
        return "Пользователи с id: " + id + " и  с id: " + friendId + ", теперь друзья";
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public String deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        service.removeFriend(id, friendId);
        return "Пользователи с id: " + id + " и  с id: " + friendId + ", больше не друзья";
    }

    @GetMapping("/{id}/friends")
    public List<User> findListFriends(@PathVariable Integer id) {
        log.debug("Текущее количество друзей пользователя: {}", service.getListFriends(id).size());
        return service.getListFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findMultiplyFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return service.getMutualFriends(id, otherId);
    }
}