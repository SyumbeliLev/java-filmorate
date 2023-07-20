package ru.yandex.practicum.filmorate.validations;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.execptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmValidationTest {
    private final FilmValidator validator = new FilmValidator();

    @Test
    public void filmCreateEmptyNameTest() { //название не может быть пустым;
        Film filmBlankName = new Film(0, "  ", "space", LocalDate.of(1995, 5, 10), 160);
        Film filmEmpty = new Film(0, "", "space", LocalDate.of(1995, 5, 10), 160);
        Film filmNullName = new Film(0, null, "space", LocalDate.of(1995, 5, 10), 160);

        ValidationException epxBlank = assertThrows(ValidationException.class, () -> validator.check(filmBlankName));
        ValidationException epxEmpty = assertThrows(ValidationException.class, () -> validator.check(filmEmpty));
        ValidationException epxNullName = assertThrows(ValidationException.class, () -> validator.check(filmNullName));

        assertEquals("Название не может быть пустым.", epxBlank.getMessage());
        assertEquals("Название не может быть пустым.", epxEmpty.getMessage());
        assertEquals("Название не может быть пустым.", epxNullName.getMessage());
    }

    @Test
    public void filmCreateFailDescription() { //максимальная длина описания — 200 символов;
        Film filmDescription200 = new Film(0, "Name", "оспода, экономическая повестка сегодняшнего дня требует определения и уточнения вывода текущих активов. Как уже неоднократно упомянуто, сделанные на базе интернет-аналитики выводы будут ассоциативно р", LocalDate.of(1995, 5, 10), 160);
        Film filmDescription201 = new Film(0, "Name", "господа, экономическая повестка сегодняшнего дня требует определения и уточнения вывода текущих активов. Как уже неоднократно упомянуто, сделанные на базе интернет-аналитики выводы будут ассоциативно р", LocalDate.of(1995, 5, 10), 160);
        Film filmDescription199 = new Film(0, "Name", "Господа, экономическая повестка сегодняшнего дня требует определения и уточнения вывода текущих активов. Как уже неоднократно упомянуто, сделанные на базе интернет-аналитики выводы будут ассоциативно", LocalDate.of(1995, 5, 10), 160);

        ValidationException epx201 = assertThrows(ValidationException.class, () -> validator.check(filmDescription201));

        assertDoesNotThrow(() -> validator.check(filmDescription199));
        assertDoesNotThrow(() -> validator.check(filmDescription200));
        assertEquals("Максимальная длина описания — 200 символов.", epx201.getMessage());
    }

    @Test
    public void filmCreateFailReleaseData() { //дата релиза — не раньше 28 декабря 1895 года;
        Film film27day = new Film(0, "name", "description", LocalDate.of(1895, 12, 27), 160);
        Film film28day = new Film(0, "name", "description", LocalDate.of(1895, 12, 28), 160);
        Film film29day = new Film(0, "name", "description", LocalDate.of(1895, 12, 29), 160);

        ValidationException exp27day = assertThrows(ValidationException.class, () -> validator.check(film27day));

        assertDoesNotThrow(() -> validator.check(film28day));
        assertDoesNotThrow(() -> validator.check(film29day));
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года.", exp27day.getMessage());
    }

    @Test
    public void filmCreateFailDuration() { //продолжительность фильма должна быть положительной.
        Film filmNegative = new Film(0, "name", "description", LocalDate.of(1995, 12, 12), -1);
        Film filmZero = new Film(0, "name", "description", LocalDate.of(1995, 12, 12), 0);
        Film filmPositive = new Film(0, "name", "description", LocalDate.of(1995, 12, 12), 1);

        ValidationException expNegative = assertThrows(ValidationException.class, () -> validator.check(filmNegative));

        assertDoesNotThrow(() -> validator.check(filmZero));
        assertDoesNotThrow(() -> validator.check(filmPositive));
        assertEquals("Продолжительность фильма должна быть положительной.", expNegative.getMessage());
    }

    @Test
    public void filmCreateValidateValue() {
        Film validFilm = new Film(0, "name", "description", LocalDate.now(), 111);
        assertDoesNotThrow(() -> validator.check(validFilm));
    }

}
