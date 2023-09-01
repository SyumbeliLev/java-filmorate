package ru.yandex.practicum.filmorate.validations;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.execption.ValidationException;
import ru.yandex.practicum.filmorate.entity.User;

import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidationTest {

    @Test
    public void createUserFailLogin() {
        User loginEmpty = User.builder().id(1L).login("").email("yandex@mail.ru").birthday(LocalDate.of(2020, 2, 2)).name("Vasa").build();
        User loginBlank = User.builder().id(1L).login("ds ds ds").email("yandex@mail.ru").birthday(LocalDate.of(2020, 2, 2)).name("Vasa").build();

        ValidationException expEmpty = assertThrows(ValidationException.class, () -> UserValidator.check(loginEmpty));
        ValidationException expBlank = assertThrows(ValidationException.class, () -> UserValidator.check(loginBlank));

        assertEquals("Логин не может быть пустым и содержать пробелы.", expEmpty.getMessage());
        assertEquals("Логин не может быть пустым и содержать пробелы.", expBlank.getMessage());
    }

    @Test
    public void createUserWithEmptyName() {
        User nameEmpty = User.builder().id(1L).login("empty").email("yandex@mail.ru").birthday(LocalDate.of(2020, 2, 2)).name("").build();
        User nameBlank = User.builder().id(1L).login("blank").email("yandex@mail.ru").birthday(LocalDate.of(2020, 2, 2)).name("  ").build();
        User nameNull = User.builder().id(1L).login("null").email("yandex@mail.ru").birthday(LocalDate.of(2020, 2, 2)).name(null).build();

        UserValidator.check(nameEmpty);
        UserValidator.check(nameBlank);
        UserValidator.check(nameNull);

        assertEquals(nameEmpty.getLogin(), nameEmpty.getName());
        assertEquals(nameBlank.getLogin(), nameBlank.getName());
        assertEquals(nameNull.getLogin(), nameNull.getName());
    }

    @Test
    public void createUserFailBirthday() {
        User dataMinusDay = User.builder().id(1L).login("login").email("yandex@mail.ru").birthday(LocalDate.now().minusDays(1)).name("name").build();
        User.UserBuilder builder = User.builder();
        builder.id(1L);
        builder.login("login");
        builder.email("yandex@mail.ru");
        builder.birthday(LocalDate.now());
        builder.name("name");
        User dataNow = builder.build();
        User dataPlusDay = User.builder().id(1L).login("login").email("yandex@mail.ru").birthday(LocalDate.now().plusDays(1)).name("name").build();

        assertDoesNotThrow(() -> UserValidator.check(dataMinusDay));
        assertDoesNotThrow(() -> UserValidator.check(dataNow));

        ValidationException expFuture = assertThrows(ValidationException.class, () -> UserValidator.check(dataPlusDay));

        assertEquals("Дата рождения не может быть в будущем.", expFuture.getMessage());

    }

    @Test
    public void createUserFailEmail() {
        User emailText = User.builder().id(1L).login("login").email("email").birthday(LocalDate.now()).name("name").build();
        User emailBlank = User.builder().id(1L).login("login").email("  ").birthday(LocalDate.now()).name("name").build();

        ValidationException expText = assertThrows(ValidationException.class, () -> UserValidator.check(emailText));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", expText.getMessage());

        ValidationException expBlank = assertThrows(ValidationException.class, () -> UserValidator.check(emailBlank));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", expBlank.getMessage());
    }

    @Test
    public void createValidUser() {
        User valid = User.builder().id(1L).login("login").email("email@mail.ru").birthday(LocalDate.now()).name("name").build();
        assertDoesNotThrow(() -> UserValidator.check(valid));
    }
}
