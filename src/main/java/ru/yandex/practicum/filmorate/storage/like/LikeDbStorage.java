package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public void likeFilm(Film film, User user) {
        try {
            jdbcTemplate.update("INSERT INTO FILM_USER_LIKE (FILM_ID, USER_ID) VALUES (?, ?)",
                    film.getId(), user.getId());
        } catch (DuplicateKeyException ex) {
            log.warn("Попытка поставить повторный лайк");
        }
    }

    @Override
    public void unlikeFilm(Film film, User user) {
        jdbcTemplate.update("DELETE FROM FILM_USER_LIKE WHERE FILM_ID = ? AND USER_ID = ?",
                film.getId(), user.getId());
    }

}
