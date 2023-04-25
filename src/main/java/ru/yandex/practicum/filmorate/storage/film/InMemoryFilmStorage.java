package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashSet;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashSet<Film> films = new HashSet<>();
    private Long id = 0L;

    @Override
    public HashSet<Film> getAllFilms() {
        return films;
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(++id);
        if (films.add(film)) {
            return film;
        }
        throw new ValidationException("Такой фильм уже есть в списке!");
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.remove(film)) {
            films.add(film);
            return film;
        }
        throw new NotFoundException("Нельзя обновить фильм, которого еще нет в списке");
    }

    @Override
    public Optional<Film> findFilmById(Long id) {
        return Optional.ofNullable(films.stream()
                .filter(film -> film.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с id %s не найден.", id))));
    }
}
