package ru.yandex.practicum.filmorate.dao;

public interface FilmLikeDao {
    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);
}
