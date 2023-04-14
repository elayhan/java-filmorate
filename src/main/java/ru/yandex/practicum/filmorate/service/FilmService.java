package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Set<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Optional<Film> findFilmById(Long id) {
        return filmStorage.findFilmById(id);
    }

    public Film likeFilm(Long filmId, Long userId) {
        Optional<Film> film = findFilmById(filmId);
        Optional<User> user = userStorage.findUserById(userId);

        if (film.isEmpty()) {
            throw new NotFoundException(String.format("Фильм с id %s не найден.", filmId));
        }

        if (user.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id %s не найден.", userId));
        }

        film.get().getLikes().add(userId);

        return film.get();
    }

    public Film unlikeFilm(Long filmId, Long userId) {
        Optional<Film> film = findFilmById(filmId);
        Optional<User> user = userStorage.findUserById(userId);

        if (film.isEmpty()) {
            throw new NotFoundException(String.format("Фильм с id %s не найден.", filmId));
        }

        if (user.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id %s не найден.", userId));
        }

        film.get().getLikes().remove(userId);

        return film.get();
    }

    public Set<Film> getMostLikedFilms(Integer count) {
        return getAllFilms().stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toSet());
    }

}
