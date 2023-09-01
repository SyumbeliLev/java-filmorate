package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.execption.FilmDoesNotExistException;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("FILMS").usingGeneratedKeyColumns("film_id");
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
        String sql = "UPDATE FILMS SET name = ?, DESCRIPTION = ?, RELEASE_DATE = ? , DURATION = ?, RATE = ?, mpa_id = ? WHERE film_id = ?";
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
    public Film getFilmById(Long filmId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE film_id = ?;", filmId);
        if (rs.next()) {
            return rowSetToFilm(rs);
        } else {
            log.info("Фильм с идентификатором {} не найден.", filmId);
            throw new FilmDoesNotExistException("Фильм с id: " + filmId + " не найден.");
        }
    }

    @Override
    public List<Film> getAllFilm() {
        String query = "SELECT * FROM films";
        Map<Long, Set<Genre>> filmIdGenres = getAllGenre();
        Map<Long, Mpa> filmIdMpa = getAllMpa();
        return jdbcTemplate.query(query, (rs, rowNum) -> Film.builder().id(rs.getLong("film_id")).name(rs.getString("name")).description(rs.getString("description")).releaseDate(Objects.requireNonNull(rs.getTimestamp("release_date")).toLocalDateTime().toLocalDate()).duration(rs.getInt("duration")).rate(rs.getInt("rate")).mpa(filmIdMpa.get(rs.getLong("film_id"))).genres(filmIdGenres.getOrDefault(rs.getLong("film_id"), new HashSet<>())).build());
    }


    @Override
    public List<Film> getPopularFilms(Integer limit) {
        String query = "SELECT f.*, COUNT(l.user_id) AS quantity FROM FILMS AS f LEFT JOIN FILM_LIKES AS l ON f.film_id = l.film_id GROUP BY f.film_id ORDER BY quantity DESC LIMIT ?";
        Map<Long, Set<Genre>> filmIdGenres = getAllGenre();
        Map<Long, Mpa> filmIdMpa = getAllMpa();
        return jdbcTemplate.query(query, (rs, rowNum) -> Film.builder().id(rs.getLong("film_id")).name(rs.getString("name")).description(rs.getString("description")).releaseDate(Objects.requireNonNull(rs.getTimestamp("release_date")).toLocalDateTime().toLocalDate()).duration(rs.getInt("duration")).rate(rs.getInt("rate")).mpa(filmIdMpa.get(rs.getLong("film_id"))).genres(filmIdGenres.getOrDefault(rs.getLong("film_id"), new HashSet<>())).build(), limit);
    }

    private void addGenreIdToFilmGenre(Set<Genre> filmGenres, Long filmId) {
        String sqlQuery = "DELETE FROM FILM_GENRES WHERE film_id = ?;";
        jdbcTemplate.update(sqlQuery, filmId);
        if (!filmGenres.isEmpty()) {
            StringBuilder queryGenre = new StringBuilder("INSERT INTO FILM_GENRES (film_id, genre_id) VALUES ");
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
        Set<Genre> genres = getFilmGenresById(filmId);
        Mpa mpa = getMpaById(filmId);
        return Film.builder().id(filmId).name(rs.getString("name")).description(rs.getString("description")).releaseDate(Objects.requireNonNull(rs.getTimestamp("release_date")).toLocalDateTime().toLocalDate()).duration(rs.getInt("duration")).rate(rs.getInt("rate")).mpa(mpa).genres(genres).build();
    }


    private Map<Long, Set<Genre>> getAllGenre() {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT film_id, FG.genre_id, name FROM FILM_GENRES as FG INNER JOIN GENRES G2 on FG.genre_id = G2.genre_id");
        Map<Long, Set<Genre>> filmsGenres = new HashMap<>();
        long filmId;
        while (rs.next()) {
            Genre genre = Genre.builder().id(rs.getInt("genre_id")).name(rs.getString("name")).build();
            Set<Genre> genres = new HashSet<>();
            filmId = rs.getLong("film_id");
            if (filmsGenres.containsKey(filmId)) {
                genres = filmsGenres.get(filmId);
            }
            genres.add(genre);
            filmsGenres.put(filmId, genres);
        }
        return filmsGenres;
    }

    private Set<Genre> getFilmGenresById(Long filmId) {
        String sqlFilm = "SELECT G2.genre_id, name FROM FILM_GENRES LEFT JOIN GENRES G2 on FILM_GENRES.genre_id = G2.genre_id WHERE film_id  = ? ORDER BY G2.genre_id ";
        return new HashSet<>(jdbcTemplate.query(sqlFilm, (rs, rowNum) -> Genre.builder().id(rs.getInt("genre_id")).name(rs.getString("name")).build(), filmId));
    }

    private Map<Long, Mpa> getAllMpa() {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT film_id, M.mpa_id, M.name FROM FILMS INNER JOIN MPA M on FILMS.mpa_id = M.mpa_id;");
        Map<Long, Mpa> filmIdMpa = new HashMap<>();
        while (rs.next()) {
            Mpa mpa = Mpa.builder().name(rs.getString("name")).id(rs.getInt("mpa_id")).build();
            filmIdMpa.put(rs.getLong("film_id"), mpa);
        }
        return filmIdMpa;
    }

    private Mpa getMpaById(Long filmId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT film_id, M.mpa_id, M.name FROM FILMS INNER JOIN MPA M on FILMS.mpa_id = M.mpa_id WHERE film_id = ?", filmId);
        Mpa mpa = null;
        if (rs.next()) {
            mpa = Mpa.builder().id(rs.getInt("mpa_id")).name(rs.getString("name")).build();
        }
        return mpa;
    }
}