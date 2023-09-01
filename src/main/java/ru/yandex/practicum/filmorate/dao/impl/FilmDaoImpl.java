package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.execption.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("FILM").usingGeneratedKeyColumns("FILM_ID");
        long filmId = simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue();

        Set<Genre> filmGenres = film.getGenres();
        if (!filmGenres.isEmpty()) {
            addGenreIdToFilmGenre(filmGenres, filmId);
        }
        film.setId(filmId);
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE FILM SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ? , DURATION = ?, RATE = ?, MPA_ID = ? WHERE FILM_ID = ?";
        Long filmID = film.getId();
        int update = jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getRate(), film.getMpa().getId(), filmID);
        Set<Genre> filmGenres = film.getGenres();

        addGenreIdToFilmGenre(filmGenres, filmID);

        if (update == 1) {
            log.info("Данные фильма с id " + filmID + " обновлены");
        } else {
            throw new FilmDoesNotExistException("Фильм с id: " + film.getId() + " не найден.");
        }
        return getFilmById(film.getId());
    }

    @Override
    public List<Film> getAll() {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM FILM");
        List<Film> filmList = new ArrayList<>();
        while (rs.next()) {
            filmList.add(rowSetToFilm(rs));
        }
        return filmList;
    }

    @Override
    public Film getFilmById(Long filmId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM FILM WHERE FILM_ID = ?;", filmId);
        if (rs.next()) {
            return rowSetToFilm(rs);
        } else {
            log.info("Фильм с идентификатором {} не найден.", filmId);
            throw new FilmDoesNotExistException("Фильм с id: " + filmId + " не найден.");
        }
    }

    private Set<Genre> getFilmGenres(Long filmId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT G2.GENRE_ID, NAME FROM FILM_GENRE LEFT JOIN GENRE G2 on FILM_GENRE.GENRE_ID = G2.GENRE_ID WHERE FILM_ID  = ? ORDER BY G2.GENRE_ID ", filmId);
        Set<Genre> genres = new HashSet<>();
        while (rs.next()) {
            Genre genre = new Genre();
            genre.setId(rs.getInt("GENRE_ID"));
            genre.setName(rs.getString("NAME"));
            genres.add(genre);
        }
        return genres;
    }

    private Mpa getMpa(Integer mpaId) {
        Mpa mpa = new Mpa();
        SqlRowSet rsMpa = jdbcTemplate.queryForRowSet("SELECT NAME FROM MPA WHERE MPA_ID = ?", mpaId);
        if (rsMpa.next()) {
            mpa.setName(rsMpa.getString("name"));
        }
        mpa.setId(mpaId);
        return mpa;
    }

    private Set<Long> getLikes(Long filmId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM FILM_LIKE WHERE FILM_ID = ?", filmId);
        Set<Long> likes = new HashSet<>();
        while (rs.next()) {
            likes.add(rs.getLong("USER_ID"));
        }
        return likes;
    }

    private void addGenreIdToFilmGenre(Set<Genre> filmGenres, Long filmId) {
        String sqlQuery = "DELETE FROM FILM_GENRE  WHERE FILM_ID = ?;";
        jdbcTemplate.update(sqlQuery, filmId);

        if (!filmGenres.isEmpty()) {
            StringBuilder queryGenre = new StringBuilder("INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES ");
            for (Genre genres : filmGenres) {
                Integer genreId = genres.getId();
                queryGenre.append("(").append(filmId).append(",").append(genreId).append("),");
            }
            queryGenre = new StringBuilder(queryGenre.substring(0, queryGenre.length() - 1));
            jdbcTemplate.update(queryGenre.toString());
        }
    }

    private Film rowSetToFilm(SqlRowSet rs) {
        Long filmId = rs.getLong("film_id");
        Set<Genre> genres = getFilmGenres(filmId);
        return Film.builder().id(filmId).name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(Objects.requireNonNull(rs.getTimestamp("release_date")).toLocalDateTime().toLocalDate())
                .duration(rs.getInt("duration"))
                .rate(rs.getInt("rate")).mpa(getMpa(rs.getInt("MPA_ID")))
                .genres(genres)
                .likes(getLikes(filmId))
                .build();
    }
}