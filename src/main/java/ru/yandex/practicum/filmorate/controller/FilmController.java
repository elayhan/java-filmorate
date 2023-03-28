package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@RestController()
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;


    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        validate(film);
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        validate(film);
        return filmService.updateFilm(film);
    }

    @GetMapping
    public Set<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{filmId}")
    public Optional<Film> findFilmById(@PathVariable Long filmId) {
        return filmService.findFilmById(filmId);
    }

    @GetMapping("/popular")
    public Set<Film> getMostLikedFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {
        return filmService.getMostLikedFilms(count);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film likeFilm(@PathVariable Long filmId,
                         @PathVariable Long userId) {
        return filmService.likeFilm(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film unlikeFilm(@PathVariable Long filmId,
                           @PathVariable Long userId) {
        return filmService.unlikeFilm(filmId, userId);
    }

    private void validate(Film film) {
        final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Указана некорректная дата релиза.");
        }
    }

}
