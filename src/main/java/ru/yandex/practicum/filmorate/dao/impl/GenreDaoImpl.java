package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.execption.GenreDoesNotExistException;

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
        String sql = "SELECT * FROM GENRES";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                Genre.builder().id(rs.getInt("genre_id")).name(rs.getString("name")).build());
    }


    private Genre rowSetToGenre(SqlRowSet rs) {
        return Genre.builder()
                .name(rs.getString("name"))
                .id(rs.getInt("genre_id"))
                .build();
    }
}
