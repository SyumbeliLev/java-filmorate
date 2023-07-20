package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.execptions.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private UserService service;
    private User userFirst;
    private User userSecond;
    private User userUpdate;

    @BeforeEach
    public void create() {
        service = new UserService();
        userFirst = User.builder().id(0).login("login").email("email@mail.ru").birthday(LocalDate.now()).name("name").build();
        userSecond = User.builder().id(0).login("login").email("email@mail.ru").birthday(LocalDate.now()).name("name").build();
        userUpdate = User.builder().id(1).login("loginUpdate").email("gmail@mail.com").birthday(LocalDate.now()).name("nameUpdate").build();
    }

    @Test
    public void createUserTest() {
        service.create(userFirst);
        assertEquals(1, service.getAll().size());
        service.create(userSecond);
        assertEquals(2, service.getAll().size());
        assertIterableEquals(List.of(userFirst, userSecond), service.getAll());
    }

    @Test
    public void updateUserTest() {
        service.create(userFirst);
        service.update(userUpdate);
        assertIterableEquals(List.of(userUpdate), service.getAll());

        int invalidID = 14;
        userUpdate.setId(invalidID);
        UserDoesNotExistException exp = assertThrows(UserDoesNotExistException.class, () -> service.update(userUpdate));
        assertEquals("Пользователь с таким id не найден.", exp.getMessage());
    }

    @Test
    public void getAllUserTest() {
        assertEquals(0, service.getAll().size());
        service.create(userSecond);
        service.create(userFirst);
        assertIterableEquals(List.of(userSecond, userFirst), service.getAll());

    }
}
