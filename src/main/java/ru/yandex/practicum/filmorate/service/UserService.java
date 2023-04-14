package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public HashSet<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public Optional<User> findUserById(Long id) {
        return userStorage.findUserById(id);
    }

    public void addFriend(Long userId, Long friendId) {
        Optional<User> user = findUserById(userId);
        Optional<User> friend = findUserById(friendId);

        if (user.isEmpty() || friend.isEmpty()) {
            throw new NotFoundException("Не найден пользователь.");
        }

        user.get().getFriends().add(friendId);
        friend.get().getFriends().add(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        Optional<User> user = findUserById(userId);
        Optional<User> friend = findUserById(friendId);

        if (user.isEmpty()) {
            throw new NotFoundException(String.format("Не найден пользователь c userId: %s", userId));
        }
        if (friend.isEmpty()) {
            throw new NotFoundException(String.format("Не найден пользователь с id: %s", friendId));
        }

        user.get().getFriends().remove(friendId);
        friend.get().getFriends().remove(userId);
    }

    public Set<User> getAllFriends(Long userId) {
        Optional<User> user = findUserById(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id %s не найден", userId));
        }

        return user.get().getFriends().stream().map(id -> findUserById(id).orElseThrow()).collect(Collectors.toSet());
    }

    public Set<User> getCommonFriends(Long userId, Long friendId) {
        Optional<User> user = findUserById(userId);
        Optional<User> friend = findUserById(friendId);

        if (user.isEmpty()) {
            throw new NotFoundException(String.format("Не найден пользователь c userId: %s", userId));
        }
        if (friend.isEmpty()) {
            throw new NotFoundException(String.format("Не найден пользователь с id: %s", friendId));
        }

        Set<Long> userFriends = user.get().getFriends();
        Set<Long> friendFriends = friend.get().getFriends();

        Set<Long> commonFriends = new HashSet<>(userFriends);
        commonFriends.retainAll(friendFriends);

        return commonFriends.stream().map(id -> findUserById(id).orElseThrow()).collect(Collectors.toSet());
    }


}
