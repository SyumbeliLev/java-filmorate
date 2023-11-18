package ru.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.filmorate.model.User;
import ru.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceImplTest {
    User vasya;
    User petya;
    User kolya;
    User masha;
    User varya;

    UserService service;

    @BeforeEach
    public void create() {
        vasya = User.builder()
                .name("Вася")
                .birthday(LocalDate.now())
                .email("Васяyandex@mail.ru")
                .login("login")
                .build();
        petya = User.builder()
                .name("Петя")
                .birthday(LocalDate.now())
                .email("Петяyandex@mail.ru")
                .login("login")
                .build();
        kolya = User.builder()
                .name("Коля")
                .birthday(LocalDate.now())
                .email("Коляyandex@mail.ru")
                .login("login")
                .build();

        masha = User.builder()
                .name("Маша")
                .birthday(LocalDate.now())
                .email("Коляyandex@mail.ru")
                .login("login")
                .build();

        varya = User.builder()
                .name("Варя")
                .birthday(LocalDate.now())
                .email("Коляyandex@mail.ru")
                .login("login")
                .build();
        service = new UserServiceImpl(new InMemoryUserStorage());
        service.createUser(vasya);
        service.createUser(petya);
        service.createUser(kolya);
        service.createUser(masha);
        service.createUser(varya);
    }

    @Test
    public void addFriendTest() {
        service.addToFriend(1, 2);
        assertTrue(petya.getFriends()
                .contains(1L));
        assertTrue(vasya.getFriends()
                .contains(2L));
        assertEquals(vasya.getFriends()
                .size(), petya.getFriends()
                .size());
    }

    @Test
    public void removeFriendTest() {
        service.addToFriend(1, 2);
        service.removeFriend(1, 2);
        assertEquals(0, petya.getFriends()
                .size());
        assertEquals(0, vasya.getFriends()
                .size());
        assertEquals(vasya.getFriends()
                .size(), petya.getFriends()
                .size());
    }

    @Test
    public void getMultiplyFriendsTest() {
        service.addToFriend(1, 2);
        service.addToFriend(1, 3);
        service.addToFriend(1, 4);

        service.addToFriend(5, 2);
        service.addToFriend(5, 3);
        service.addToFriend(5, 4);

        assertEquals(List.of(petya, kolya, masha), service.getMutualFriends(1, 5));
        assertTrue(vasya.getFriends()
                .containsAll(varya.getFriends()));
    }

    @Test
    public void getListFriendsTest() {
        service.addToFriend(1, 2);
        service.addToFriend(1, 3);
        service.addToFriend(1, 4);

        assertEquals(List.of(petya, kolya, masha), service.getListFriends(1));
        assertEquals(3, service.getListFriends(1)
                .size());
    }
}
