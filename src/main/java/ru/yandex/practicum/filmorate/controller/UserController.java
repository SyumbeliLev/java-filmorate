package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;

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
    public User findUserById(@PathVariable Long id) {
        return service.getUserById(id);
    }

    @GetMapping
    public List<User> findAll() {
        log.debug("Текущее количество пользователей: {}", service.getALlUser().size());
        return service.getALlUser();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public String addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        service.addToFriend(id, friendId);
        return "Пользователи с id: " + id + " и  с id: " + friendId + ", теперь друзья";
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public String deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        service.removeFriend(id, friendId);
        return "Пользователи с id: " + id + " и  с id: " + friendId + ", больше не друзья";
    }

    @GetMapping("/{id}/friends")
    public List<User> findListFriends(@PathVariable Long id) {
        log.debug("Текущее количество друзей пользователя: {}", service.getListFriends(id).size());
        return service.getListFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findMultiplyFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return service.getMutualFriends(id, otherId);
    }
}