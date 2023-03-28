package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.Optional;

public interface UserStorage {
    User createUser(User user);

    HashSet<User> getAllUsers();

    User updateUser(User user);

    Optional<User> findUserById(Long id);

}
