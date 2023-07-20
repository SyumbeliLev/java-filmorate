package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")

public class UserController {
    private final UserService service = new UserService();

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        service.create(user);
        log.info("Добавление пользователя:" + user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        service.update(user);
        log.info("Обновление пользователя с id: {}", user.getId());
        return user;
    }

    @GetMapping
    public List<User> findAll() {
        log.debug("Текущее количество пользователей: {}", service.getAll().size());
        return service.getAll();
    }
}
