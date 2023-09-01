package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.execption.GenreDoesNotExistException;
import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(Integer genreId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES WHERE genre_id = ?;", genreId);
        if (rs.next()) {
            return rowSetToGenre(rs);
        } else {
            log.info("Жанр с идентификатором {} не найден.", genreId);
            throw new GenreDoesNotExistException("Жанр с id: " + genreId + " не найден.");
        }
    }

    @Override
    public List<Genre> getAllGenre() {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES");
        List<Genre> genresList = new ArrayList<>();
        while (rs.next()) {
            genresList.add(rowSetToGenre(rs));
        }
        return genresList;
    }

    private Genre rowSetToGenre(SqlRowSet rs) {
        Genre genre = new Genre();
        genre.setId(rs.getInt("genre_id"));
        genre.setName(rs.getString("name"));
        return genre;
    }
}
