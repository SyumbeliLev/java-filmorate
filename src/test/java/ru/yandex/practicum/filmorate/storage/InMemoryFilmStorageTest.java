package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.execption.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryFilmStorageTest {
    private FilmService service;
    private Film filmFirst;
    private Film filmSecond;
    private Film filmUpdate;

    @BeforeEach
    public void create() {
        service = new FilmService(new InMemoryFilmStorage(new FilmValidator()));
        filmFirst = Film.builder()
                .id(1)
                .name("name")
                .description("DESCRIPTION")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();
        filmSecond = Film.builder()
                .id(2)
                .name("name")
                .description("DESCRIPTION")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();
        filmUpdate = Film.builder()
                .id(1)
                .name("name")
                .description("DESCRIPTION")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();
    }

    @Test
    public void createFilmTest() {
        service.getStorage().create(filmFirst);
        assertEquals(1, service.getStorage().getAll().size());
        service.getStorage().create(filmSecond);
        assertEquals(2, service.getStorage().getAll().size());
        assertIterableEquals(List.of(filmFirst, filmSecond), service.getStorage().getAll());
    }

    @Test
    public void updateFilmTest() {
        service.getStorage().create(filmFirst);
        service.getStorage().update(filmUpdate);
        assertIterableEquals(List.of(filmUpdate), service.getStorage().getAll());

        int invalidID = 14;
        filmUpdate.setId(invalidID);
        FilmDoesNotExistException exp = assertThrows(FilmDoesNotExistException.class, () -> service.getStorage().update(filmUpdate));
        assertEquals("Фильм с id: " + invalidID + " не найден.", exp.getMessage());
    }

    @Test
    public void getAllFilmTest() {
        assertEquals(0, service.getStorage().getAll().size());
        service.getStorage().create(filmSecond);
        service.getStorage().create(filmFirst);
        assertIterableEquals(List.of(filmSecond, filmFirst), service.getStorage().getAll());

    }

    @Test
    public void getFilmById(){
        FilmDoesNotExistException notFound = assertThrows(FilmDoesNotExistException.class, () -> service.getStorage().getFilmById(1));
        assertEquals("Фильм с id: 1 не найден.",notFound.getMessage());

        service.getStorage().create(filmFirst);
        assertEquals(filmFirst, service.getStorage().getFilmById(1));
    }
}
