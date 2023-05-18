package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.GenreRowMapper;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GenreDBStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Genre> findGenreById(int id) {
        String querySQL = "SELECT GENRE_ID, GENRE_NAME FROM GENRE WHERE GENRE_ID = ?";
        Genre genre;
        try {
            genre = jdbcTemplate.queryForObject(querySQL, new GenreRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            genre = null;
        }
        return Optional.ofNullable(genre);
    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("SELECT GENRE_ID, GENRE_NAME FROM GENRE", new GenreRowMapper());
    }
}
