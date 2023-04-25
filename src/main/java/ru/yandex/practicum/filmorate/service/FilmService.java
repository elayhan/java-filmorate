package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final UserService userService;

    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;


    public Film createFilm(Film film) {
        log.debug("Создаем фильм {}", film);
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        log.debug("Изменяем фильм {}", film);
        return filmStorage.updateFilm(film);
    }

    public Set<Film> getAllFilms() {
        log.debug("Получаем список всех фильмов");
        return filmStorage.getAllFilms();
    }

    public Film findFilmById(long id) {
        log.debug("Ищем фильм с id {}", id);
        return filmStorage.findFilmById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм c id %s не найден", id)));
    }

    public Film likeFilm(long filmId, long userId) {
        Film film = findFilmById(filmId);
        User user = userService.findUserById(userId);

        likeStorage.likeFilm(film, user);

        return findFilmById(filmId);
    }

    public Film unlikeFilm(long filmId, long userId) {
        Film film = findFilmById(filmId);
        User user = userService.findUserById(userId);

        likeStorage.unlikeFilm(film, user);

        return findFilmById(filmId);
    }

    public Set<Film> getMostLikedFilms(Integer count) {
        return getAllFilms().stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toSet());
    }

}
