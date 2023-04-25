package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.GenreRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;


    private List<Long> getUserIdLikedFilmById(long filmId) {
        String getUserIdLikedSQL = "SELECT USER_ID FROM FILM_USER_LIKE WHERE FILM_ID = ?";
        return jdbcTemplate.queryForList(getUserIdLikedSQL, Long.class, filmId);
    }

    @Override
    public Set<Film> getAllFilms() {
        Set<Film> films = new HashSet<>(jdbcTemplate.query("SELECT f.FILM_ID" +
                ", f.NAME" +
                ", f.DESCRIPTION" +
                ", f.RELEASE_DATE " +
                ", f.DURATION " +
                ", f.RATE_ID " +
                ", mr.RATE_NAME " +
                "FROM FILMS f " +
                "LEFT JOIN MPAA_RATE mr ON f.RATE_ID  = mr.RATE_ID " +
                "ORDER BY 1", new FilmRowMapper()));

        films.forEach(film -> {
            film.getGenres().addAll(getGenreList(film.getId()));
            film.getLikes().addAll(getUserIdLikedFilmById(film.getId()));
        });

        return films;
    }

    private void addFilmGenre(long filmId, int genreId) {
        String addFilmGenreSQL = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?,?)";

        try {
            jdbcTemplate.update(addFilmGenreSQL, filmId, genreId);
        } catch (DuplicateKeyException ignored) {

        }
    }

    @Override
    public Film createFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String insertFilmSQL = "INSERT INTO FILMS (NAME, DESCRIPTION,RELEASE_DATE,DURATION,RATE_ID) " +
                "VALUES (?,?,?,?,?)";

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertFilmSQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() == null) {
            throw new NotFoundException("Не удалось получить id созданного фильма");
        }

        long filmId = keyHolder.getKey().longValue();
        film.getGenres().forEach(genre -> addFilmGenre(filmId, genre.getId()));

        return findFilmById(filmId).orElseThrow();
    }

    @Override
    public Film updateFilm(Film film) {
        String updateSQL = "UPDATE FILMS SET NAME = ?" +
                ", DESCRIPTION = ?" +
                ", RELEASE_DATE = ?" +
                ", DURATION = ?" +
                ", RATE_ID = ? " +
                "WHERE FILM_ID = ?";

        int cnt = jdbcTemplate.update(updateSQL, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());

        if (cnt == 0) {
            throw new NotFoundException(String.format("Фильм с id %s не найден", film.getId()));
        }

        jdbcTemplate.update("DELETE FROM FILM_GENRE WHERE FILM_ID = ?", film.getId());
        film.getGenres().forEach(genre -> addFilmGenre(film.getId(), genre.getId()));

        return findFilmById(film.getId()).orElseThrow();
    }

    @Override
    public Optional<Film> findFilmById(Long id) {
        String querySQL = "SELECT f.FILM_ID" +
                ", f.NAME" +
                ", f.DESCRIPTION " +
                ",f.RELEASE_DATE " +
                ",f.DURATION " +
                ",f.RATE_ID " +
                ",mr.RATE_NAME " +
                "FROM FILMS f \n" +
                "LEFT JOIN MPAA_RATE mr ON f.RATE_ID  = mr.RATE_ID " +
                "WHERE FILM_ID = ?";
        try {
            Film film = jdbcTemplate.queryForObject(querySQL, new FilmRowMapper(), id);
            if (film != null) {
                List<Genre> genres = getGenreList(id);
                film.getGenres().addAll(genres);
                film.getLikes().addAll(getUserIdLikedFilmById(id));
            }
            return Optional.ofNullable(film);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @NotNull
    private List<Genre> getGenreList(Long id) {
        String getGenreSQL = "SELECT g.GENRE_ID, g.GENRE_NAME  FROM GENRE g \n" +
                "JOIN FILM_GENRE fg ON g.GENRE_ID = fg.GENRE_ID \n" +
                "WHERE fg.FILM_ID = ?" +
                "ORDER BY 1";
        return jdbcTemplate.query(getGenreSQL, new GenreRowMapper(), id);
    }

}
