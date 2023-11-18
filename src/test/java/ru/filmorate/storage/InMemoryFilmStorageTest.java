package ru.filmorate.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.filmorate.model.Film;
import ru.yandex.practicum.filmorate.execption.FilmDoesNotExistException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryFilmStorageTest {
    private InMemoryFilmStorage storage;
    private Film filmFirst;
    private Film filmSecond;
    private Film filmUpdate;

    @BeforeEach
    public void create() {
        storage = new InMemoryFilmStorage();
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
        storage.create(filmFirst);
        assertEquals(1, storage.getAll()
                .size());
        storage.create(filmSecond);
        assertEquals(2, storage.getAll()
                .size());
        assertIterableEquals(List.of(filmFirst, filmSecond), storage.getAll());
    }

    @Test
    public void updateFilmTest() {
        storage.create(filmFirst);
        storage.update(filmUpdate);
        assertIterableEquals(List.of(filmUpdate), storage.getAll());

        int invalidID = 14;
        filmUpdate.setId(invalidID);
        FilmDoesNotExistException exp = assertThrows(FilmDoesNotExistException.class, () -> storage.update(filmUpdate));
        assertEquals("Фильм с id: " + invalidID + " не найден.", exp.getMessage());
    }

    @Test
    public void getAllFilmTest() {
        assertEquals(0, storage.getAll()
                .size());
        storage.create(filmSecond);
        storage.create(filmFirst);
        assertIterableEquals(List.of(filmSecond, filmFirst), storage.getAll());

    }

    @Test
    public void getFilmById() {
        FilmDoesNotExistException notFound = assertThrows(FilmDoesNotExistException.class, () -> storage.getFilmById(1));
        assertEquals("Фильм с id: 1 не найден.", notFound.getMessage());

        storage.create(filmFirst);
        Assertions.assertEquals(filmFirst, storage.getFilmById(1));
    }
}
