package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.execption.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryUserStorageTest {
    private UserService service;
    private User userFirst;
    private User userSecond;
    private User userUpdate;

    @BeforeEach
    public void create() {
        service = new UserService(new InMemoryUserStorage(new UserValidator()));
        userFirst = User.builder().id(0).login("login").email("email@mail.ru").birthday(LocalDate.now()).name("name").build();
        userSecond = User.builder().id(0).login("login").email("email@mail.ru").birthday(LocalDate.now()).name("name").build();
        userUpdate = User.builder().id(1).login("loginUpdate").email("gmail@mail.com").birthday(LocalDate.now()).name("nameUpdate").build();
    }

    @Test
    public void createUserTest() {
        service.getStorage().create(userFirst);
        assertEquals(1, service.getStorage().getAll().size());
        service.getStorage().create(userSecond);
        assertEquals(2, service.getStorage().getAll().size());
        assertIterableEquals(List.of(userFirst, userSecond), service.getStorage().getAll());
    }

    @Test
    public void updateUserTest() {
        service.getStorage().create(userFirst);
        service.getStorage().update(userUpdate);
        assertIterableEquals(List.of(userUpdate), service.getStorage().getAll());

        int invalidID = 14;
        userUpdate.setId(invalidID);
        UserDoesNotExistException exp = assertThrows(UserDoesNotExistException.class, () -> service.getStorage().update(userUpdate));
        assertEquals("Пользователь с id " + invalidID + " не найден.", exp.getMessage());
    }

    @Test
    public void getAllUserTest() {
        assertEquals(0, service.getStorage().getAll().size());
        service.getStorage().create(userSecond);
        service.getStorage().create(userFirst);
        assertIterableEquals(List.of(userSecond, userFirst), service.getStorage().getAll());

    }

    @Test
    public void getUserById() {
        UserDoesNotExistException notFound = assertThrows(UserDoesNotExistException.class, () -> service.getStorage().getUserById(1));
        assertEquals("Пользователь с id 1 не найден.", notFound.getMessage());

        service.getStorage().create(userFirst);
        assertEquals(userFirst, service.getStorage().getUserById(1));
    }
}
