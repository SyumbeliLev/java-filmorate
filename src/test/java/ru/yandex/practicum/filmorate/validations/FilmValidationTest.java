package ru.yandex.practicum.filmorate.validations;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.execption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmValidationTest {
    

    @Test
    public void filmCreateEmptyNameTest() { //название не может быть пустым;
        Film filmBlankName = Film.builder()
                .name(" ")
                .description("DESCRIPTION")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(100)
                .build();
        Film filmEmpty = Film.builder()
                .name(" ")
                .description("DESCRIPTION")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(100)
                .build();
        Film filmNullName = Film.builder()
                .name(null)
                .description("DESCRIPTION")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(100)
                .build();

        ValidationException epxBlank = assertThrows(ValidationException.class, () -> FilmValidator.check(filmBlankName));
        ValidationException epxEmpty = assertThrows(ValidationException.class, () -> FilmValidator.check(filmEmpty));
        ValidationException epxNullName = assertThrows(ValidationException.class, () -> FilmValidator.check(filmNullName));

        assertEquals("Название не может быть пустым.", epxBlank.getMessage());
        assertEquals("Название не может быть пустым.", epxEmpty.getMessage());
        assertEquals("Название не может быть пустым.", epxNullName.getMessage());
    }

    @Test
    public void filmCreateFailDescription() { //максимальная длина описания — 200 символов;
        Film filmDescription200 = Film.builder()
                .name("name")
                .description("оспода, экономическая повестка сегодняшнего дня требует определения и уточнения вывода текущих активов. Как уже неоднократно упомянуто, сделанные на базе интернет-аналитики выводы будут ассоциативно р")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();

        Film filmDescription201 = Film.builder()
                .name("name")
                .description("господа, экономическая повестка сегодняшнего дня требует определения и уточнения вывода текущих активов. Как уже неоднократно упомянуто, сделанные на базе интернет-аналитики выводы будут ассоциативно р")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();


        Film filmDescription199 = Film.builder()
                .name("name")
                .description("господа, экономическая повестка сегодшнего дня требует определения и уточнения вывода текущих активов. Как уже неоднократно упомянуто, сделанные на базе интернет-аналитики выводы будут ассоциативно р")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();
        ValidationException epx201 = assertThrows(ValidationException.class, () -> FilmValidator.check(filmDescription201));

        assertDoesNotThrow(() -> FilmValidator.check(filmDescription199));
        assertDoesNotThrow(() -> FilmValidator.check(filmDescription200));
        assertEquals("Максимальная длина описания — 200 символов.", epx201.getMessage());
    }

    @Test

    public void filmCreateFailReleaseData() { //дата релиза — не раньше 28 декабря 1895 года;
        Film film27day = Film.builder()
                .name("name")
                .description("DESCRIPTION")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(100)
                .build();
        Film film28day = Film.builder()
                .name("name")
                .description("DESCRIPTION")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(100)
                .build();
        Film film29day = Film.builder()
                .name("name")
                .description("DESCRIPTION")
                .releaseDate(LocalDate.of(1895, 12, 29))
                .duration(100)
                .build();

        ValidationException exp27day = assertThrows(ValidationException.class, () -> FilmValidator.check(film27day));

        assertDoesNotThrow(() -> FilmValidator.check(film28day));
        assertDoesNotThrow(() -> FilmValidator.check(film29day));
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года.", exp27day.getMessage());
    }

    @Test
    public void filmCreateFailDuration() { //продолжительность фильма должна быть положительной.
        Film filmNegative = Film.builder()
                .name("name")
                .description("DESCRIPTION")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(-1)
                .build();

        Film filmZero = Film.builder()
                .name("name")
                .description("DESCRIPTION")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(0)
                .build();
        Film filmPositive = Film.builder()
                .name("name")
                .description("DESCRIPTION")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(1)
                .build();

        ValidationException expNegative = assertThrows(ValidationException.class, () -> FilmValidator.check(filmNegative));

        assertDoesNotThrow(() -> FilmValidator.check(filmZero));
        assertDoesNotThrow(() -> FilmValidator.check(filmPositive));
        assertEquals("Продолжительность фильма должна быть положительной.", expNegative.getMessage());
    }

    @Test
    public void filmCreateValidateValue() {
        Film validFilm = Film.builder()
                .name("name")
                .description("DESCRIPTION")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(100)
                .build();
        assertDoesNotThrow(() -> FilmValidator.check(validFilm));
    }

}
