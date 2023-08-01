package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceTest {
    User Vasya;
    User Petya;
    User Kolya;
    User Masha;
    User Varya;

    UserService service;

    @BeforeEach
    public void create() {
        Vasya = User.builder()
                .name("Вася")
                .birthday(LocalDate.now())
                .email("Васяyandex@mail.ru")
                .login("login")
                .build();
        Petya = User.builder()
                .name("Петя")
                .birthday(LocalDate.now())
                .email("Петяyandex@mail.ru")
                .login("login")
                .build();
        Kolya = User.builder()
                .name("Коля")
                .birthday(LocalDate.now())
                .email("Коляyandex@mail.ru")
                .login("login")
                .build();

        Masha = User.builder()
                .name("Маша")
                .birthday(LocalDate.now())
                .email("Коляyandex@mail.ru")
                .login("login")
                .build();

        Varya = User.builder()
                .name("Варя")
                .birthday(LocalDate.now())
                .email("Коляyandex@mail.ru")
                .login("login")
                .build();
        service = new UserService(new InMemoryUserStorage(new UserValidator()));
        service.getStorage().create(Vasya);
        service.getStorage().create(Petya);
        service.getStorage().create(Kolya);
        service.getStorage().create(Masha);
        service.getStorage().create(Varya);
    }

    @Test
    public void addFriendTest() {
        service.addToFriend(1, 2);
        assertTrue(Petya.getFriends().contains(1L));
        assertTrue(Vasya.getFriends().contains(2L));
        assertEquals(Vasya.getFriends().size(), Petya.getFriends().size());
    }

    @Test
    public void removeFriendTest() {
        service.addToFriend(1, 2);
        service.removeFriend(1, 2);
        assertEquals(0, Petya.getFriends().size());
        assertEquals(0, Vasya.getFriends().size());
        assertEquals(Vasya.getFriends().size(), Petya.getFriends().size());
    }

    @Test
    public void getMultiplyFriendsTest() {
        service.addToFriend(1, 2);
        service.addToFriend(1, 3);
        service.addToFriend(1, 4);

        service.addToFriend(5, 2);
        service.addToFriend(5, 3);
        service.addToFriend(5, 4);

        assertEquals(List.of(Petya, Kolya, Masha), service.getMutualFriends(1, 5));
        assertTrue(Vasya.getFriends().containsAll(Varya.getFriends()));
    }

    @Test
    public void getListFriendsTest() {
        service.addToFriend(1, 2);
        service.addToFriend(1, 3);
        service.addToFriend(1, 4);

        assertEquals(List.of(Petya, Kolya, Masha), service.getListFriends(1));
        assertEquals(3, service.getListFriends(1).size());
    }
}
