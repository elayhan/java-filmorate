package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashSet<User> users = new HashSet<>();
    private Long id = 0L;

    @Override
    public User createUser(User user) {
        user.setId(++id);
        if (users.add(user)) {
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
            return user;
        }
        throw new NotFoundException("Нельзя обновить пользователя который не существует! Сначала создайте его.");
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

}
