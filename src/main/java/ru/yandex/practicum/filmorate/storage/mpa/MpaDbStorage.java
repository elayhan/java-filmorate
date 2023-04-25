package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mapper.MpaRowMapper;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Mpa> findMpaById(int id) {
        String getMpaByIdSQL = "select rate_id, rate_name from mpaa_rate where rate_id = ?";
        Mpa mpa = null;
        try {
            mpa = jdbcTemplate.queryForObject(getMpaByIdSQL, new MpaRowMapper(), id);
        } catch (EmptyResultDataAccessException ignored) {

        }

        return Optional.ofNullable(mpa);
    }

    @Override
    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query("select rate_id, rate_name from mpaa_rate order by 1", new MpaRowMapper());
    }
}
