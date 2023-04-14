package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Optional;
import java.util.Set;

public interface FilmStorage {
    public Set<Film> getAllFilms();

    public Film createFilm(Film film);

    public Film updateFilm(Film film);

    public Optional<Film> findFilmById(Long id);

}
