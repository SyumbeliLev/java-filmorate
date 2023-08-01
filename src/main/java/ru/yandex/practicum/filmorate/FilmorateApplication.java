package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilmorateApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilmorateApplication.class, args);
    /*    User user1 = User.builder()
                .name("Вася")
                .birthday(LocalDate.now())
                .email("Васяyandex@mail.ru")
                .login("login")
                .build();
        User user2 = User.builder()
                .name("Петя")
                .birthday(LocalDate.now())
                .email("Петяyandex@mail.ru")
                .login("login")
                .build();
        User user3 = User.builder()
                .name("Коля")
                .birthday(LocalDate.now())
                .email("Коляyandex@mail.ru")
                .login("login")
                .build();

        User user4 = User.builder()
                .name("Маша")
                .birthday(LocalDate.now())
                .email("Коляyandex@mail.ru")
                .login("login")
                .build();

        User user5 = User.builder()
                .name("Варя")
                .birthday(LocalDate.now())
                .email("Коляyandex@mail.ru")
                .login("login")
                .build();
        InMemoryUserStorage userStorage = new InMemoryUserStorage(new UserValidator());
        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);
        userStorage.create(user4);
        userStorage.create(user5);


        UserService userService = new UserService(userStorage);

        userService.addToFriend(user1.getId(), user2.getId());
        userService.addToFriend(user1.getId(), user3.getId());
        userService.addToFriend(user1.getId(), user4.getId());
        userService.addToFriend(user5.getId(), user2.getId());
        userService.addToFriend(user5.getId(), user3.getId());
        userService.addToFriend(user5.getId(), user4.getId());

        System.out.println( userService.getMutualFriends(8, 2));
*/
    }

}
