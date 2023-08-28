package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.execption.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmDaoImpl implements FilmDao {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public void create(Film film) {
        String sql = "INSERT INTO PUBLIC.FILM (NAME, DESCRIPTION, RELEASE_DATE, DURATION) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());

    }

    @Override
    public void update(Film film) {
        String sql = "UPDATE PUBLIC.FILM SET NAME = ?, DESCRIPTION = ?, NAME = ?, RELEASE_DATE = ? , DURATION = ? WHERE FILM_ID = ?";
        int filmID = film.getId();
        int update = jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getName(), film.getReleaseDate(), film.getDuration(), filmID);
        if (update == 1) {
            log.info("Данные фильма с id " + filmID + " обновлены");
        } else {
            throw new FilmDoesNotExistException("Фильм с id: " + film.getId() + " не найден.");
        }
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT * FROM PUBLIC.FILM";
        return jdbcTemplate.query(sql, (rs, rowNum) -> Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getTimestamp("release_date").toLocalDateTime().toLocalDate())
                .duration(rs.getInt("duration"))
                .build());
    }

    @Override
    public Film getFilmById(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM PUBLIC.FILM WHERE FILM_ID = ?", id);

        if (filmRows.next()) {
            return Film.builder()
                    .id(filmRows.getInt("film_id"))
                    .name(filmRows.getString("name"))
                    .description(filmRows.getString("description"))
                    .releaseDate(Objects.requireNonNull(filmRows.getTimestamp("release_date")).toLocalDateTime().toLocalDate())
                    .duration(filmRows.getInt("duration"))
                    .build();
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new FilmDoesNotExistException("Фильм с id: " + id + " не найден.");
        }
    }
}