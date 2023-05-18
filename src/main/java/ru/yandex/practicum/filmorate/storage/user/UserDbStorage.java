package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Optional;

@Component
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User createUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String insertUserSql = "insert into users (email, login, name, birthday) values (?,?,?,?)";
        try {
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getEmail());
                ps.setString(2, user.getLogin());
                ps.setString(3, user.getName());
                ps.setDate(4, Date.valueOf(user.getBirthday()));
                return ps;
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            throw new ValidationException("Такой пользователь уже существует");
        }

        return findUserById((Long) keyHolder.getKey()).orElseThrow();
    }

    @Override
    public HashSet<User> getAllUsers() {
        return new HashSet<>(jdbcTemplate.query("Select * from users", new UserRowMapper()));
    }

    @Override
    public User updateUser(User user) {
        String updateSql = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?";
        int cnt = jdbcTemplate.update(updateSql,
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        if (cnt == 0) {
            throw new NotFoundException(String.format("Пользователь c id %s не найден", user.getId()));
        }
        return user;
    }

    @Override
    public Optional<User> findUserById(Long id) {
        String querySql = "select * from users where user_id = ?";
        User user;
        try {
            user = jdbcTemplate.queryForObject(querySql, new UserRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            user = null;
        }

        return Optional.ofNullable(user);
    }
}
