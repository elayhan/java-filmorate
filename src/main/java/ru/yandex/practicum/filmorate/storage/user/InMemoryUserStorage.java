package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final HashSet<User> users = new HashSet<>();
    private Long id = 0L;

    @Override
    public User createUser(User user) {
        user.setId(++id);
        if (users.add(user)) {
            log.debug("Добавлен пользователь {}", user);
            return user;
        }
        throw new ValidationException("Такой пользователь уже существует.");
    }

    @Override
    public HashSet<User> getAllUsers() {
        return users;
    }

    @Override
    public User updateUser(User user) {
        if (users.remove(user)) {
            users.add(user);
            log.debug("Изменен пользователь id: {} на {}", user.getId(), user);
            return user;
        }
        throw new NotFoundException("Нельзя обновить пользователя который не существует! Сначала создайте его.");
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(users.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id %s не найден", id))));
    }

}
