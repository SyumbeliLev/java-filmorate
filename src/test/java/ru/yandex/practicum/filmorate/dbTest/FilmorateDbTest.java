package ru.yandex.practicum.filmorate.dbTest;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmorateDbTest {

    private final UserDao userDao;
    private final UserService userService;
    private final FilmService filmService;
    private final FilmDao filmDao;
    private final FriendsDao friendsDao;
    private final FilmLikeDao filmLikeDao;
    private final MpaDao mpaDao;
    private final GenreDao genreDao;
    User userAlex1;
    User userEgor2;
    User userAnna3;
    User userOlga4;
    Film filmAllHatesCris;
    Film filmTomAndJerry;
    Film filmDiamondHand;
    Mpa mpa1;
    Mpa mpa2;
    Mpa mpa3;
    Set<Genre> genres;

    @BeforeEach
    public void create() {
        userAlex1 = User.builder()
                .email("Alex@yandex.ru")
                .login("alex")
                .name("Alexandr Ivanov")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userEgor2 = User.builder()
                .email("Egor@yandex.ru")
                .login("egor")
                .name("egor")
                .birthday(LocalDate.of(2000, 3, 3))
                .build();
        userAnna3 = User.builder()
                .email("Anna@yandex.ru")
                .login("anna")
                .name("Anna Smith")
                .birthday(LocalDate.of(2000, 12, 12))
                .build();
        userOlga4 = User.builder()
                .email("Olga@yandex.ru")
                .login("olga")
                .name("Olga Smith")
                .birthday(LocalDate.of(2002, 1, 1))
                .build();
        mpa1 = new Mpa();
        mpa2 = new Mpa();
        mpa3 = new Mpa();
        mpa1.setId(1);
        mpa1.setName("J");
        mpa2.setId(2);
        mpa2.setName("J");
        mpa3.setId(1);
        mpa3.setName("J");
        Genre genre1 = new Genre();
        genre1.setId(1);
        genre1.setName("Комедия");
        Genre genre2 = new Genre();
        genre2.setId(3);
        genre2.setName("Мультфильм");
        genres = Set.of(genre1, genre2);
        filmAllHatesCris = Film.builder()
                .name("All hates Cris")
                .description("Good comedy")
                .duration(30)
                .releaseDate(LocalDate.of(1998, 1, 1))
                .mpa(mpa1)
                .genres(genres)
                .likes(new HashSet<>())
                .build();
        filmDiamondHand = Film.builder()
                .name("Diamond hand")
                .description("Good comedy")
                .duration(90)
                .releaseDate(LocalDate.of(1978, 1, 1))
                .mpa(mpa2)
                .genres(new HashSet<>())
                .likes(new HashSet<>())
                .build();
        filmTomAndJerry = Film.builder()
                .name("Tom and Jerry")
                .description("Children cartoon")
                .duration(10)
                .releaseDate(LocalDate.of(1998, 1, 1))
                .mpa(mpa3)
                .genres(new HashSet<>())
                .likes(new HashSet<>())
                .build();


    }


    @Test
    public void shouldcreateAndFindUserById() {

        User user1 = userDao.create(userAlex1);
        Optional<User> userOptional = Optional.ofNullable(userDao.getUserById(user1.getId()));
        assertThat(userOptional)
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", user.getId())
                                .hasFieldOrPropertyWithValue("email", "Alex@yandex.ru")
                                .hasFieldOrPropertyWithValue("name", "Alexandr Ivanov")
                                .hasFieldOrPropertyWithValue("login", "alex")
                                .hasFieldOrPropertyWithValue("birthday",
                                        LocalDate.of(2000, 10, 10)));
    }


    @Test
    public void shouldcreateWithEmptyNameAndFindUserById() {

        User user1 = userDao.create(userEgor2);
        Optional<User> userOptional = Optional.ofNullable(userDao.getUserById(user1.getId()));
        assertThat(userOptional)
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", user.getId())
                                .hasFieldOrPropertyWithValue("email", "Egor@yandex.ru")
                                .hasFieldOrPropertyWithValue("name", "egor")
                                .hasFieldOrPropertyWithValue("login", "egor")
                                .hasFieldOrPropertyWithValue("birthday",
                                        LocalDate.of(2000, 3, 3)));
    }

    @Test
    public void shouldupdate() {

        User user1 = userDao.create(userAlex1);
        User userAlex1Updated = User.builder()
                .id(user1.getId())
                .email("AlexSmith@google.ru")
                .login("alex")
                .name("Alex Smith")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        User user1Updated = userDao.update(userAlex1Updated);
        Optional<User> userOptional = Optional.ofNullable(userDao.getUserById(user1Updated.getId()));
        assertThat(userOptional)
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", user.getId())
                                .hasFieldOrPropertyWithValue("email", "AlexSmith@google.ru")
                                .hasFieldOrPropertyWithValue("name", "Alex Smith")
                                .hasFieldOrPropertyWithValue("login", "alex")
                                .hasFieldOrPropertyWithValue("birthday",
                                        LocalDate.of(2000, 10, 10)));
    }


    @Test
    public void shouldListUsers() {

        User user1 = userDao.create(userAlex1);
        User user2 = userDao.create(userEgor2);
        User user3 = userDao.create(userAnna3);

        List<User> listUsers = userDao.getAll();

        assertThat(listUsers).asList().hasSize(3);

        assertThat(listUsers).asList().contains(userDao.getUserById(user1.getId()));
        assertThat(listUsers).asList().contains(userDao.getUserById(user2.getId()));
        assertThat(listUsers).asList().contains(userDao.getUserById(user3.getId()));

        assertThat(Optional.of(listUsers.get(0)))
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user)
                                .hasFieldOrPropertyWithValue("login", "alex"));

        assertThat(Optional.of(listUsers.get(1)))
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user)
                                .hasFieldOrPropertyWithValue("login", "egor"));

        assertThat(Optional.of(listUsers.get(2)))
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user)
                                .hasFieldOrPropertyWithValue("login", "anna"));
    }

    @Test
    public void shouldGetEmptyListUsers() {

        List<User> listUsers = userDao.getAll();

        assertThat(listUsers).asList().hasSize(0);
        assertThat(listUsers).asList().isEmpty();

    }


    @Test
    public void shouldAddFriend() {

        User user1 = userDao.create(userAlex1);
        User user2 = userDao.create(userEgor2);

        friendsDao.addToFriend(user1.getId(), user2.getId());

        List<User> listFriends = friendsDao.getListFriends(user1.getId());

        assertThat(listFriends).asList().hasSize(1);
        assertThat(listFriends).asList().contains(userDao.getUserById(user2.getId()));

        assertThat(Optional.of(listFriends.get(0)))
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user)
                                .hasFieldOrPropertyWithValue("email", "Egor@yandex.ru"));


    }

    @Test
    public void shouldDeleteFriend() {

        User user1 = userDao.create(userAlex1);
        User user2 = userDao.create(userEgor2);

        userService.addToFriend(user1.getId(), user2.getId());
        userService.removeFriend(user1.getId(), user2.getId());

        List<User> listFriends = userService.getListFriends(user1.getId());

        assertThat(listFriends).asList().hasSize(0);
        assertThat(listFriends).asList().doesNotContain(userAlex1);

    }

    @Test
    public void shouldGetCommonFriends1() {

        User user1 = userDao.create(userAlex1);
        User user2 = userDao.create(userEgor2);
        User user3 = userDao.create(userAnna3);


        userService.addToFriend(user1.getId(), user3.getId());
        userService.addToFriend(user2.getId(), user3.getId());
        List<User> commonFriends = userService.getMutualFriends(user1.getId(), user2.getId());

        assertThat(commonFriends).asList().hasSize(1);
        assertThat(commonFriends).asList().contains(userDao.getUserById(user3.getId()));

        assertThat(Optional.of(commonFriends.get(0)))
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user)
                                .hasFieldOrPropertyWithValue("email", "Anna@yandex.ru"));


    }

    @Test
    public void shouldGetCommonFriends2() {

        User user1 = userDao.create(userAlex1);
        User user2 = userDao.create(userEgor2);
        User user3 = userDao.create(userAnna3);
        User user4 = userDao.create(userOlga4);

        userService.addToFriend(user1.getId(), user3.getId());
        userService.addToFriend(user2.getId(), user3.getId());
        userService.addToFriend(user1.getId(), user4.getId());
        userService.addToFriend(user2.getId(), user4.getId());

        List<User> commonFriends = userService.getMutualFriends(user1.getId(), user2.getId());

        assertThat(commonFriends).asList().hasSize(2);

        assertThat(commonFriends).asList().contains(userDao.getUserById(user3.getId()));
        assertThat(commonFriends).asList().contains(userDao.getUserById(user4.getId()));

        assertThat(Optional.of(commonFriends.get(0)))
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user)
                                .hasFieldOrPropertyWithValue("email", "Anna@yandex.ru"));

        assertThat(Optional.of(commonFriends.get(0)))
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user)
                                .hasFieldOrPropertyWithValue("email", "Anna@yandex.ru"));


    }


    @Test
    public void shouldAddFilmAndFindFilmById() {

        Film film = filmDao.create(filmAllHatesCris);

        Optional<Film> filmOptional = Optional.ofNullable(filmDao.getFilmById(film.getId()));
        assertThat(filmOptional)
                .hasValueSatisfying(f ->
                        assertThat(f)
                                .hasFieldOrPropertyWithValue("id", film.getId())
                                .hasFieldOrPropertyWithValue("name", "All hates Cris")
                                .hasFieldOrPropertyWithValue("description", "Good comedy")
                                .hasFieldOrPropertyWithValue("releaseDate",
                                        LocalDate.of(1998, 1, 1))
                                .hasFieldOrPropertyWithValue("duration",
                                        30)
                                .hasFieldOrPropertyWithValue("mpa.id", 1));

    }

    @Test
    public void shouldUpdateFilm() {
        Film film = filmDao.create(filmAllHatesCris);
        film.setName("Update");
        filmDao.create(film);
        Optional<Film> filmOptional = Optional.ofNullable(filmDao.getFilmById(film.getId()));
        assertThat(filmOptional)
                .hasValueSatisfying(f ->
                        assertThat(f)
                                .hasFieldOrPropertyWithValue("id", film.getId())
                                .hasFieldOrPropertyWithValue("name", "Update")
                                .hasFieldOrPropertyWithValue("description", "Good comedy")
                                .hasFieldOrPropertyWithValue("releaseDate",
                                        LocalDate.of(1998, 1, 1))
                                .hasFieldOrPropertyWithValue("duration",
                                        30)
                                .hasFieldOrPropertyWithValue("mpa.id", 1)
                                .hasFieldOrPropertyWithValue("genres", genres));
    }

    @Test
    public void shouldUpdate2Film() {

        Film film = filmDao.create(filmAllHatesCris);

        // обновляем жанры и Mpa
        Set<Genre> genresNew = new HashSet<>();
        Genre genre = new Genre();
        genre.setId(1);
        genre.setName("Комедия");
        genresNew.add(genre);
        Mpa mpaNew = new Mpa();
        mpaNew.setId(4);
        mpaNew.setName("R");
        film.setGenres(genresNew);
        film.setMpa(mpaNew);
        film.setName("UPDATED");
        filmDao.update(film);

        Optional<Film> filmOptional = Optional.ofNullable(filmDao.getFilmById(film.getId()));
        assertThat(filmOptional)
                .hasValueSatisfying(f ->
                        assertThat(f)
                                .hasFieldOrPropertyWithValue("id", film.getId())
                                .hasFieldOrPropertyWithValue("name", "UPDATED")
                                .hasFieldOrPropertyWithValue("description", "Good comedy")
                                .hasFieldOrPropertyWithValue("releaseDate",
                                        LocalDate.of(1998, 1, 1))
                                .hasFieldOrPropertyWithValue("duration",
                                        30)
                                .hasFieldOrPropertyWithValue("mpa.id", 4)
                                .hasFieldOrPropertyWithValue("mpa.name", "R")
                                .hasFieldOrPropertyWithValue("genres", genresNew));
    }

    @Test
    public void shouldListFilms() {

        Film film1 = filmDao.create(filmAllHatesCris);
        Film film2 = filmDao.create(filmDiamondHand);
        Film film3 = filmDao.create(filmTomAndJerry);


        List<Film> listFilms = filmDao.getAll();

        assertThat(listFilms).asList().hasSize(3);

        assertThat(listFilms).asList().contains(filmDao.getFilmById(film1.getId()));
        assertThat(listFilms).asList().contains(filmDao.getFilmById(film2.getId()));
        assertThat(listFilms).asList().contains(filmDao.getFilmById(film3.getId()));

        assertThat(Optional.of(listFilms.get(0)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "All hates Cris"));

        assertThat(Optional.of(listFilms.get(1)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Diamond hand"));

        assertThat(Optional.of(listFilms.get(2)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Tom and Jerry"));

    }

    @Test
    public void shouldListFilmsEmpty() {

        List<Film> listFilms = filmDao.getAll();

        assertThat(listFilms).asList().hasSize(0);
        assertThat(listFilms).asList().isEmpty();
    }


    @Test
    public void shouldAddLike() {
        User user1 = userDao.create(userAlex1);
        Film film1 = filmDao.create(filmAllHatesCris);

        filmLikeDao.addLike(film1.getId(), user1.getId());
        Integer likes = film1.getLikes().size();

        assertThat(likes).isEqualTo(0);
    }

    @Test
    public void shouldDeleteLike() {
        User user1 = userDao.create(userAlex1);
        Film film1 = filmDao.create(filmAllHatesCris);

        filmLikeDao.addLike(film1.getId(), user1.getId());
        filmLikeDao.removeLike(film1.getId(), user1.getId());

        Integer likes = film1.getLikes().size();

        assertThat(likes).isEqualTo(0);

    }

    @Test
    public void shouldListMostPopularFilms() {

        User user1 = userDao.create(userAlex1);
        User user2 = userDao.create(userEgor2);
        User user3 = userDao.create(userAnna3);

        Film film1 = filmDao.create(filmAllHatesCris);
        Film film2 = filmDao.create(filmDiamondHand);
        Film film3 = filmDao.create(filmTomAndJerry);

        filmLikeDao.addLike(film1.getId(), user1.getId());
        filmLikeDao.addLike(film1.getId(), user2.getId());
        filmLikeDao.addLike(film1.getId(), user3.getId());
        filmLikeDao.addLike(film2.getId(), user1.getId());

        List<Film> listFilms = filmService.getPopularFilms(10);

        assertThat(listFilms).asList().hasSize(3);


        assertThat(listFilms).asList().startsWith(filmDao.getFilmById(film1.getId()));
        assertThat(listFilms).asList().contains(filmDao.getFilmById(film2.getId()));
        assertThat(listFilms).asList().endsWith(filmDao.getFilmById(film3.getId()));

        assertThat(Optional.of(listFilms.get(0)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "All hates Cris"));

        assertThat(Optional.of(listFilms.get(1)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Diamond hand"));

        assertThat(Optional.of(listFilms.get(2)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Tom and Jerry"));
    }

    @Test
    public void shouldListMostPopularFilms2() {
        User user1 = userDao.create(userAlex1);
        User user2 = userDao.create(userEgor2);
        User user3 = userDao.create(userAnna3);

        Film film1 = filmDao.create(filmAllHatesCris);
        Film film2 = filmDao.create(filmDiamondHand);
        Film film3 = filmDao.create(filmTomAndJerry);

        filmLikeDao.addLike(film1.getId(), user1.getId());
        filmLikeDao.addLike(film1.getId(), user2.getId());
        filmLikeDao.addLike(film1.getId(), user3.getId());
        filmLikeDao.addLike(film2.getId(), user1.getId());

        List<Film> listFilms = filmService.getPopularFilms(2);

        assertThat(listFilms).asList().hasSize(2);

        assertThat(listFilms).asList().startsWith(filmDao.getFilmById(film1.getId()));
        assertThat(listFilms).asList().endsWith(filmDao.getFilmById(film2.getId()));
        assertThat(listFilms).asList().doesNotContain(filmDao.getFilmById(film3.getId()));

        assertThat(Optional.of(listFilms.get(0)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "All hates Cris"));

        assertThat(Optional.of(listFilms.get(1)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Diamond hand"));


    }


    @Test
    public void shouldListMpa() {
        List<Mpa> listMpa = mpaDao.getAllMpa();
        assertThat(listMpa).asList().hasSize(5);

        final Integer firstId = 1;
        final Integer lastId = 5;

        assertThat(listMpa).asList().startsWith(mpaDao.getMpaById(firstId));
        assertThat(listMpa).asList().endsWith(mpaDao.getMpaById(lastId));

    }

    @Test
    public void getMpaById() {

        final Integer secondId = 2;
        final String secondName = "PG";

        assertThat(mpaDao.getMpaById(secondId))
                .hasFieldOrPropertyWithValue("id", 2)
                .hasFieldOrPropertyWithValue("name", secondName);
    }

    @Test
    public void shouldListGenres() {

        List<Genre> listGenres = genreDao.getAllGenre();

        assertThat(listGenres).asList().hasSize(6);

        final Integer firstId = 1;
        final Integer lastId = 6;

        assertThat(listGenres).asList().startsWith(genreDao.getGenreById(firstId));
        assertThat(listGenres).asList().endsWith(genreDao.getGenreById(lastId));

    }

    @Test
    public void getGenreById() {

        final Integer id = 5;
        final String name5 = "Документальный";

        assertThat(genreDao.getGenreById(id))
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("name", name5);
    }
}