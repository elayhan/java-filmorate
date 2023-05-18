package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.genre.GenreDBStorage;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreStorageTest {
    private final GenreDBStorage storage;

    @Test
    void findGenreById() {
        assertThat(storage.findGenreById(1))
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre)
                                .hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("name", "Комедия"));
    }

    @Test
    void findGenreByNotExistsId() {
        assertThat(storage.findGenreById(999)).isEmpty();
    }

    @Test
    void getAllGenres() {
        assertThat(storage.getAllGenres().size()).isGreaterThan(0);
    }

}
