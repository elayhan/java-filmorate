package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    void deleteAllFilms() {
        jdbcTemplate.update("delete from films");
        jdbcTemplate.execute("alter table films alter column film_id restart with 1");
    }

    Film film = Film.builder()
            .name("1")
            .releaseDate(LocalDate.of(2000, 1, 1))
            .duration(2)
            .mpa(Mpa.builder().id(1).build())
            .build();

    @Test
    void createFilm() {
        film.getGenres().add(Genre.builder().id(1).build());
        film.getGenres().add(Genre.builder().id(1).name("Комедия").build());
        film.getGenres().add(Genre.builder().id(1).name("Драма").build());
        assertThat(filmDbStorage.createFilm(film)).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    void updateFilm() {
        film = filmDbStorage.createFilm(film);
        assertThat(film).hasFieldOrPropertyWithValue("name", "1");
        film.setName("2");
        Film updateFilm = filmDbStorage.updateFilm(film);
        assertThat(updateFilm).hasFieldOrPropertyWithValue("name", "2");
    }

    @Test
    void updateNotExistsFilm() {
        film.setId(999L);
        Exception ex = assertThrows(NotFoundException.class, () -> filmDbStorage.updateFilm(film));
        assertThat(ex.getMessage()).isEqualTo("Фильм с id 999 не найден");
    }

    @Test
    void findFilmById() {
        filmDbStorage.createFilm(film);
        assertThat(filmDbStorage.findFilmById(1L))
                .isPresent()
                .hasValueSatisfying(film1 -> assertThat(film1)
                        .hasFieldOrPropertyWithValue("id", 1L)
                        .hasFieldOrPropertyWithValue("name", "1")
                        .hasFieldOrPropertyWithValue("duration", 2));
    }

    @Test
    void findFilmByNoExistsId() {
        assertThat(filmDbStorage.findFilmById(999L)).isEmpty();
    }

    @Test
    void getAllFilms() {
        assertThat(filmDbStorage.getAllFilms()).size().isEqualTo(0);
        filmDbStorage.createFilm(film);
        assertThat(filmDbStorage.getAllFilms()).size().isEqualTo(1);

        filmDbStorage.createFilm(film);
        assertThat(filmDbStorage.getAllFilms()).size().isEqualTo(2);

    }
}
