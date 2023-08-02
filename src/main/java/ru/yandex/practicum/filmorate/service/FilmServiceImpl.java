package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.execption.IncorrectParameterException;
import ru.yandex.practicum.filmorate.execption.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmServiceImpl implements FilmService {
    private final InMemoryFilmStorage storage;

    @Autowired
    public FilmServiceImpl(InMemoryFilmStorage storage) {
        this.storage = storage;
    }


    public void createFilm(Film film){
        FilmValidator.check(film);
        storage.create(film);
    }

    public void updateFilm(Film film) {
        FilmValidator.check(film);
        storage.update(film);
    }

    public List<Film> getAllFilm(){
        return storage.getAll();
    }

    public Film getFilmById(Integer id){
        return storage.getFilmById(id);
    }

    public void addLike(Integer filmId, Integer userId) {
        Film film = storage.getFilmById(filmId);
        film.getLikes().add(Long.valueOf(userId));
    }

    public void removeLike(Integer filmId, Integer userId) {
        Film film = storage.getFilmById(filmId);
        if (film.getLikes().contains(Long.valueOf(userId))) {
            film.getLikes().remove(Long.valueOf(userId));
        } else {
            throw new UserDoesNotExistException("Пользователь с id: " + userId + " не найден");
        }

    }

    public List<Film> getPopularFilms(Integer count) {
        if (count <= 0) {
            throw new IncorrectParameterException(count.toString());
        }
        return storage.getAll().stream().sorted((p0, p1) -> {
            int likesValue1 = p0.getLikes().size();
            int likesValue2 = p1.getLikes().size();
            return Integer.compare(likesValue2, likesValue1);
        }).limit(count).collect(Collectors.toList());
    }
}
