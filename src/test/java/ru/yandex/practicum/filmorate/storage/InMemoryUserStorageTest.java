package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.execption.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryUserStorageTest {
    private InMemoryUserStorage storage;
    private User userFirst;
    private User userSecond;
    private User userUpdate;

    @BeforeEach
    public void create() {
        storage = new InMemoryUserStorage();
        userFirst = User.builder().id(0).login("login").email("email@mail.ru").birthday(LocalDate.now()).name("name").build();
        userSecond = User.builder().id(0).login("login").email("email@mail.ru").birthday(LocalDate.now()).name("name").build();
        userUpdate = User.builder().id(1).login("loginUpdate").email("gmail@mail.com").birthday(LocalDate.now()).name("nameUpdate").build();
    }

    @Test
    public void createUserTest() {
        storage.create(userFirst);
        assertEquals(1, storage.getAll().size());
        storage.create(userSecond);
        assertEquals(2, storage.getAll().size());
        assertIterableEquals(List.of(userFirst, userSecond), storage.getAll());
    }

    @Test
    public void updateUserTest() {
        storage.create(userFirst);
        storage.update(userUpdate);
        assertIterableEquals(List.of(userUpdate), storage.getAll());

        int invalidID = 14;
        userUpdate.setId(invalidID);
        UserDoesNotExistException exp = assertThrows(UserDoesNotExistException.class, () -> storage.update(userUpdate));
        assertEquals("Пользователь с id " + invalidID + " не найден.", exp.getMessage());
    }

    @Test
    public void getAllUserTest() {
        assertEquals(0, storage.getAll().size());
        storage.create(userSecond);
        storage.create(userFirst);
        assertIterableEquals(List.of(userSecond, userFirst), storage.getAll());

    }

    @Test
    public void getUserById() {
        UserDoesNotExistException notFound = assertThrows(UserDoesNotExistException.class, () -> storage.getUserById(1));
        assertEquals("Пользователь с id 1 не найден.", notFound.getMessage());

        storage.create(userFirst);
        assertEquals(userFirst, storage.getUserById(1));
    }
}
