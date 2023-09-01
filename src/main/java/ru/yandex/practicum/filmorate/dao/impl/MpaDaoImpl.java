package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.execption.MpaDoesNotExistException;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getMpaById(Integer mpaId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM MPA WHERE mpa_id = ?;", mpaId);
        if (rs.next()) {
            return rowSetToMpa(rs);
        } else {
            log.info("Возрастное ограничение с идентификатором {} не найден.", mpaId);
            throw new MpaDoesNotExistException("Возрастное ограничение с id: " + mpaId + " не найден.");
        }
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM MPA";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                Mpa.builder().id(rs.getInt("mpa_id")).name(rs.getString("name")).build());
    }

    private Mpa rowSetToMpa(SqlRowSet rs) {
        return Mpa.builder()
                .id(rs.getInt("mpa_id"))
                .name(rs.getString("name"))
                .build();
    }
}
