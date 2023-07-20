package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.execptions.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FilmServiceTest {
    private FilmService service;
    private Film filmFirst;
    private Film filmSecond;
    private Film filmUpdate;

    @BeforeEach
    public void create() {
        service = new FilmService();
        filmFirst = new Film(0, "name", "description", LocalDate.now(), 111);
        filmSecond = new Film(0, "name", "description", LocalDate.now(), 111);
        filmUpdate = new Film(1, "NAME", "description", LocalDate.now(), 175);
    }

    @Test
    public void createFilmTest() {
        service.create(filmFirst);
        assertEquals(1, service.getAll().size());
        service.create(filmSecond);
        assertEquals(2, service.getAll().size());
        assertIterableEquals(List.of(filmFirst, filmSecond), service.getAll());
    }

    @Test
    public void updateFilmTest() {
        service.create(filmFirst);
        service.update(filmUpdate);
        assertIterableEquals(List.of(filmUpdate), service.getAll());

        int invalidID = 14;
        filmUpdate.setId(invalidID);
        FilmDoesNotExistException exp = assertThrows(FilmDoesNotExistException.class, () -> service.update(filmUpdate));
        assertEquals("Фильм с таким id не найден.", exp.getMessage());
    }

    @Test
    public void getAllFilmTest() {
        assertEquals(0, service.getAll().size());
        service.create(filmSecond);
        service.create(filmFirst);
        assertIterableEquals(List.of(filmSecond, filmFirst), service.getAll());

    }
}
