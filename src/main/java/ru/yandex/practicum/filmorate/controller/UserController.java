package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashSet;

@RestController()
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final HashSet<User> users = new HashSet<>();
    private int id = 0;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        user.setId(++id);
        validate(user);
        if (users.add(user)) {
            log.debug("Добавлен пользователь {}", user);
            return user;
        }
        throw new UserException("Пользователь уже существует");
    }

    @GetMapping
    public HashSet<User> getAllUsers() {
        return users;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (users.remove(user)) {
            validate(user);
            users.add(user);
            log.debug("Изменен пользователь id: {} на {}", user.getId(), user);
            return user;
        }
        throw new UserException("Нельзя обновить пользователя который не существует! Сначала создайте его.");
    }

    private void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

}
