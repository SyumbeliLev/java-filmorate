package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@RequiredArgsConstructor
class UserServiceImplTest {
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
        service.createUser(vasya);
        service.createUser(petya);
        service.createUser(kolya);
        service.createUser(masha);
        service.createUser(varya);
    }

    @Test
    public void addFriendTest() {
        service.addToFriend(1L, 2L);
        assertTrue(petya.getFriends().contains(1L));
        assertTrue(vasya.getFriends().contains(2L));
        assertEquals(vasya.getFriends().size(), petya.getFriends().size());
    }

    @Test
    public void removeFriendTest() {
        service.addToFriend(1L, 2L);
        service.removeFriend(1L, 2L);
        assertEquals(0L, petya.getFriends().size());
        assertEquals(0L, vasya.getFriends().size());
        assertEquals(vasya.getFriends().size(), petya.getFriends().size());
    }

    @Test
    public void getMultiplyFriendsTest() {
        service.addToFriend(1L, 2L);
        service.addToFriend(1L, 3L);
        service.addToFriend(1L, 4L);

        service.addToFriend(5L, 2L);
        service.addToFriend(5L, 3L);
        service.addToFriend(5L, 4L);

        assertEquals(List.of(petya, kolya, masha), service.getMutualFriends(1L, 5L));
        assertTrue(vasya.getFriends().containsAll(varya.getFriends()));
    }

    @Test
    public void getListFriendsTest() {
        service.addToFriend(1L, 2L);
        service.addToFriend(1L, 3L);
        service.addToFriend(1L, 4L);

        assertEquals(service.getListFriends(1L), List.of(petya, kolya, masha));
        assertEquals(3L, service.getListFriends(1L).size());
    }
}
