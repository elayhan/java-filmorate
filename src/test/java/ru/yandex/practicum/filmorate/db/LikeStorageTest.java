package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikeStorageTest {
    private final LikeDbStorage storage;
    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;
    private final JdbcTemplate jdbcTemplate;

    User user = User.builder()
            .email("@@@1111@@@@")
            .login("sd")
            .name("ds")
            .birthday(LocalDate.of(2000, 1, 1))
            .build();

    Film film = Film.builder()
            .name("1")
            .releaseDate(LocalDate.of(2000, 1, 1))
            .duration(2)
            .mpa(Mpa.builder().id(1).build())
            .build();

    @BeforeEach
    void deleteAllFilms() {
        jdbcTemplate.update("delete from films");
        jdbcTemplate.execute("alter table films alter column film_id restart with 1");
        jdbcTemplate.update("delete from users");
        jdbcTemplate.execute("alter table users alter column user_id restart with 1");
    }

    @Test
    void likeFilm() {
        user = userDbStorage.createUser(user);
        film = filmDbStorage.createFilm(film);

        storage.likeFilm(film, user);

        assertThat(filmDbStorage.findFilmById(1L)).isPresent()
                .hasValueSatisfying(film1 -> assertThat(film1.getLikes().size()).isEqualTo(1));
    }

    @Test
    void unlikeFilm() {
        user = userDbStorage.createUser(user);
        film = filmDbStorage.createFilm(film);

        storage.likeFilm(film, user);
        storage.likeFilm(film, user);

        assertThat(filmDbStorage.findFilmById(1L))
                .isPresent()
                .hasValueSatisfying(film1 -> assertThat(film1.getLikes().size()).isEqualTo(1));

        storage.unlikeFilm(film, user);

        assertThat(filmDbStorage.findFilmById(1L))
                .isPresent()
                .hasValueSatisfying(film1 -> assertThat(film1.getLikes().size()).isEqualTo(0));
    }

}
