package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.execptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")

public class UserController {
    Map<Integer,User> users = new HashMap<>();
    int nextId = 1;

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user){
        User validUser = checkValidation(user);
        validUser.setId(nextId);
        users.put(nextId, validUser);
        log.info("Добавление пользователя:" + validUser);
        nextId++;
        return new ResponseEntity<> (validUser, HttpStatusCode.valueOf(200));
    }

    @PutMapping
    public ResponseEntity<User> update(@RequestBody User user){
        User validUser = checkValidation(user);
        if(users.containsKey(user.getId())){
            users.put(user.getId(), validUser);
            return new ResponseEntity<> (validUser, HttpStatusCode.valueOf(200));
        }else
            return new ResponseEntity<> (validUser, HttpStatusCode.valueOf(HttpStatus.SC_NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<Collection<User>> findAll() {
        log.c("Текущее количество пользователей: {}", users.size());
        return new ResponseEntity<>(users.values(),HttpStatusCode.valueOf(200));
    }





    private User checkValidation(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() & !user.getEmail().contains("@")) {
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @.");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("дата рождения не может быть в будущем.");
        }
        return user;
    }
}
