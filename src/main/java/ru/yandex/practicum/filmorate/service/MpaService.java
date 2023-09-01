package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaService {
    Mpa getMpaById(Integer id);

    List<Mpa> getAllMpa();
}
