package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Service
@RequiredArgsConstructor
class MpaServiceImpl implements MpaService {
    private final MpaDao mpaDao;

    @Override
    public Mpa getMpaById(Integer id) {
        return mpaDao.getMpaById(id);
    }

    @Override
    public List<Mpa> getAllMpa() {
        return mpaDao.getAllMpa();
    }
}
