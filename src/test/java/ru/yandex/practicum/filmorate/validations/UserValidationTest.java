package ru.yandex.practicum.filmorate.validations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.execptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidationTest {
    private final UserValidator validator = new UserValidator();

    @Test
    public void createUserFailLogin() { // логин не может быть пустым и содержать пробелы;
        User loginEmpty = User.builder().id(1).login("").email("yandex@mail.ru").birthday(LocalDate.of(2020, 2, 2)).name("Vasa").build();
        User loginBlank = User.builder().id(1).login("ds ds ds").email("yandex@mail.ru").birthday(LocalDate.of(2020, 2, 2)).name("Vasa").build();

        ValidationException expEmpty = assertThrows(ValidationException.class, () -> validator.check(loginEmpty));
        ValidationException expBlank = assertThrows(ValidationException.class, () -> validator.check(loginBlank));

        assertEquals("Логин не может быть пустым и содержать пробелы.", expEmpty.getMessage());
        assertEquals("Логин не может быть пустым и содержать пробелы.", expBlank.getMessage());
    }

    @Test
    public void createUserWithEmptyName() {//имя для отображения может быть пустым — в таком случае использовать логин;
        User nameEmpty = User.builder().id(1).login("empty").email("yandex@mail.ru").birthday(LocalDate.of(2020, 2, 2)).name("").build();
        User nameBlank = User.builder().id(1).login("blank").email("yandex@mail.ru").birthday(LocalDate.of(2020, 2, 2)).name("  ").build();
        User nameNull = User.builder().id(1).login("null").email("yandex@mail.ru").birthday(LocalDate.of(2020, 2, 2)).name(null).build();

        validator.check(nameEmpty);
        validator.check(nameBlank);
        validator.check(nameNull);

        assertEquals(nameEmpty.getLogin(), nameEmpty.getName());
        assertEquals(nameBlank.getLogin(), nameBlank.getName());
        assertEquals(nameNull.getLogin(), nameNull.getName());
    }

    @Test
    public void createUserFailBirthday() { //дата рождения не может быть в будущем.
        User dataMinusDay = User.builder().id(1).login("login").email("yandex@mail.ru").birthday(LocalDate.now().minusDays(1)).name("name").build();
        User dataNow = User.builder().id(1).login("login").email("yandex@mail.ru").birthday(LocalDate.now()).name("name").build();
        User dataPlusDay = User.builder().id(1).login("login").email("yandex@mail.ru").birthday(LocalDate.now().plusDays(1)).name("name").build();

        assertDoesNotThrow(() -> validator.check(dataMinusDay));
        assertDoesNotThrow(() -> validator.check(dataNow));

        ValidationException expFuture = assertThrows(ValidationException.class, () -> validator.check(dataPlusDay));

        assertEquals("Дата рождения не может быть в будущем.", expFuture.getMessage());

    }

    @Test
    public void createUserFailEmail() {
        User emailText = User.builder().id(1).login("login").email("email").birthday(LocalDate.now()).name("name").build();
        User emailBlank = User.builder().id(1).login("login").email("  ").birthday(LocalDate.now()).name("name").build();

        ValidationException expText = assertThrows(ValidationException.class, () -> validator.check(emailText));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", expText.getMessage());

        ValidationException expBlank = assertThrows(ValidationException.class, () -> validator.check(emailBlank));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", expBlank.getMessage());
    }

    @Test
    public void createValidUser(){
        User valid = User.builder().id(1).login("login").email("email@mail.ru").birthday(LocalDate.now()).name("name").build();
        assertDoesNotThrow(() -> validator.check(valid));
    }
}
