package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmServiceTest {
    private final FilmService filmService = new FilmService(new InMemoryFilmStorage(new FilmValidator()));
    public Film filmEmptyLike;
    public Film film1;
    public Film film2;
    public Film film3;
    public Film film4;
    public Film film5;
    public Film film6;
    public Film film7;
    public Film film8;
    public Film film9;
    public Film film10;

    @BeforeEach
    public void create() {
        filmEmptyLike = Film.builder().name("name").description("DESCRIPTION").releaseDate(LocalDate.of(1895, 12, 30)).duration(100).build();
        film1 = Film.builder().name("name").description("DESCRIPTION").releaseDate(LocalDate.of(1895, 12, 30)).duration(100).likes(Set.of(1L)).build();
        film2 = Film.builder().name("name").description("DESCRIPTION").releaseDate(LocalDate.of(1895, 12, 30)).duration(100).likes(Set.of(1L, 2L)).build();
        film3 = Film.builder().name("name").description("DESCRIPTION").releaseDate(LocalDate.of(1895, 12, 30)).duration(100).likes(Set.of(1L, 2L, 3L)).build();
        film4 = Film.builder().name("name").description("DESCRIPTION").releaseDate(LocalDate.of(1895, 12, 30)).duration(100).likes(Set.of(1L, 2L, 3L, 4L)).build();
        film5 = Film.builder().name("name").description("DESCRIPTION").releaseDate(LocalDate.of(1895, 12, 30)).duration(100).likes(Set.of(1L, 2L, 3L, 4L, 5L)).build();
        film6 = Film.builder().name("name").description("DESCRIPTION").releaseDate(LocalDate.of(1895, 12, 30)).duration(100).likes(Set.of(1L, 2L, 3L, 4L, 5L, 6L)).build();
        film7 = Film.builder().name("name").description("DESCRIPTION").releaseDate(LocalDate.of(1895, 12, 30)).duration(100).likes(Set.of(1L, 2L, 3L, 4L, 5L, 6L, 7L)).build();
        film8 = Film.builder().name("name").description("DESCRIPTION").releaseDate(LocalDate.of(1895, 12, 30)).duration(100).likes(Set.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L)).build();
        film9 = Film.builder().name("name").description("DESCRIPTION").releaseDate(LocalDate.of(1895, 12, 30)).duration(100).likes(Set.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L)).build();
        film10 = Film.builder().name("name").description("DESCRIPTION").releaseDate(LocalDate.of(1895, 12, 30)).duration(100).likes(Set.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L)).build();
        filmService.getStorage().create(filmEmptyLike);
        filmService.getStorage().create(film1);
        filmService.getStorage().create(film2);
        filmService.getStorage().create(film3);
        filmService.getStorage().create(film4);
        filmService.getStorage().create(film5);
        filmService.getStorage().create(film6);
        filmService.getStorage().create(film7);
        filmService.getStorage().create(film8);
        filmService.getStorage().create(film9);
        filmService.getStorage().create(film10);
    }

    @Test
    public void addlLikeTest() {
        assertEquals(Set.of(), filmEmptyLike.getLikes());

        filmService.addLike(1, 1);
        assertEquals(1, filmEmptyLike.getLikes().size());
    }

    @Test
    public void removeLikeTest() {
        filmService.addLike(1, 1);
        filmService.removeLike(1, 1);
        assertEquals(0, filmEmptyLike.getLikes().size());
    }

    @Test
    public void getPopularFilmsTest() {
        assertEquals(List.of(film10,film9,film8,film7,film6,film5,film4,film3,film2,film1), filmService.getPopularFilms(10));
    }
}
