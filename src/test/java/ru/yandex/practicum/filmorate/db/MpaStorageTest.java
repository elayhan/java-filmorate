package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaStorageTest {
    private final MpaDbStorage storage;

    @Test
    void findMpaById(){
        assertThat(storage.findMpaById(1)).isPresent().hasValueSatisfying(mpa -> assertThat(mpa)
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "G"));
    }

    @Test
    void findMpaByNotExistsId(){
        assertThat(storage.findMpaById(999)).isEmpty();
    }

    @Test
    void getAllMpa(){
        assertThat(storage.getAllMpa().size()).isEqualTo(5);
    }

}
