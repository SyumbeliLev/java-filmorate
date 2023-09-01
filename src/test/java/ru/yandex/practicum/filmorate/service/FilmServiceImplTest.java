package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
public class FilmServiceImplTest {
    private final FilmService service;
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
        service.createFilm(filmEmptyLike);
        service.createFilm(film1);
        service.createFilm(film2);
        service.createFilm(film3);
        service.createFilm(film4);
        service.createFilm(film5);
        service.createFilm(film6);
        service.createFilm(film7);
        service.createFilm(film8);
        service.createFilm(film9);
        service.createFilm(film10);
    }

    @Test
    public void addLikeTest() {
        assertEquals(Set.of(), filmEmptyLike.getLikes());

        service.addLike(1L, 1L);
        assertEquals(1, filmEmptyLike.getLikes().size());
    }

    @Test
    public void removeLikeTest() {
        service.addLike(1L, 1L);
        service.removeLike(1L, 1L);
        assertEquals(0, filmEmptyLike.getLikes().size());
    }

    @Test
    public void getPopularFilmsTest() {
        assertEquals(List.of(film10, film9, film8, film7, film6, film5, film4, film3, film2, film1), service.getPopularFilms(10));
    }
}
