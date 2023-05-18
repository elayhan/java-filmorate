package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public interface LikeStorage {
    void likeFilm(Film film, User user);

    void unlikeFilm(Film film, User user);

}
