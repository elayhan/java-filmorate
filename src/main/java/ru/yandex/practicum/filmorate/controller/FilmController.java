package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashSet;

@RestController()
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final HashSet<Film> films = new HashSet<>();
    private final static LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private int id = 0;

    @GetMapping
    public HashSet<Film> getAllFilms() {
        return films;
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        film.setId(++id);
        validate(film);
        if (films.add(film)) {
            log.debug("Добавлен фильм: {}", film);
            return film;
        }
        throw new FilmException("Такой фильм уже есть в списке!");
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (films.remove(film)) {
            validate(film);
            films.add(film);
            log.debug("Изменен фильм id: {} на {}", film.getId(), film);
            return film;
        }
        throw new FilmException("Нельзя обновить фильм, которого еще нет в списке");
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new FilmException("Указана некорректная дата релиза!");
        }
    }
}
