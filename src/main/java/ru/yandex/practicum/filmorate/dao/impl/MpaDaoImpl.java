package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.execption.MpaDoesNotExistException;
import ru.yandex.practicum.filmorate.entity.Mpa;

import java.util.ArrayList;
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
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM MPA");
        List<Mpa> mpaList = new ArrayList<>();
        while (rs.next()) {
            mpaList.add(rowSetToMpa(rs));
        }
        return mpaList;
    }

    private Mpa rowSetToMpa(SqlRowSet rs) {
        Mpa mpa = new Mpa();
        mpa.setId(rs.getInt("mpa_id"));
        mpa.setName(rs.getString("name"));
        return mpa;
    }
}
