package ru.yandex.practicum.filmorate;


import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.web.bind.MethodArgumentNotValidException;

import ru.yandex.practicum.filmorate.controller.FilmController;

import ru.yandex.practicum.filmorate.execptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class FilmorateApplicationTests {

    @Test
    void contextLoads() {
    }

}
