package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.execption.GenreDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(Integer genreId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM GENRE WHERE GENRE_ID = ?;", genreId);
        if (rs.next()) {
            return rowSetToGenre(rs);
        } else {
            log.info("Жанр с идентификатором {} не найден.", genreId);
            throw new GenreDoesNotExistException("Жанр с id: " + genreId + " не найден.");
        }
    }

    @Override
    public List<Genre> getAllGenre() {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM GENRE");
        List<Genre> genresList = new ArrayList<>();
        while (rs.next()){
            genresList.add(rowSetToGenre(rs));
        }
        return genresList;
    }

    private Genre rowSetToGenre(SqlRowSet rs) {
        Genre genre = new Genre();
        genre.setId(rs.getInt("GENRE_ID"));
        genre.setName(rs.getString("NAME"));
        return genre;
    }
}
