package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    public Genre findGenreById(int id) {
        log.debug("Получим жанр с id {}", id);
        return genreStorage.findGenreById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Жанр с id %s не найден", id)));
    }

    public List<Genre> getAllGenres() {
        log.debug("Получим все жанры");
        return genreStorage.getAllGenres();
    }

}
