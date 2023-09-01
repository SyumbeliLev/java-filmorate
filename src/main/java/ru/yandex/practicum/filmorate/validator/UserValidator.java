package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.execption.ValidationException;

import java.time.LocalDate;

@Slf4j
public class UserValidator {
    public static void check(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Не валидная почта");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @.");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Не валидный логин");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Не валидная дата рождения");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
