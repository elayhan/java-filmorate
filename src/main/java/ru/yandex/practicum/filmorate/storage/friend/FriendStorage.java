package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface FriendStorage {
    void addFriend(User currentUser, User addingUser);

    void removeFriend(User currentUser, User removeUser);

    Set<User> getAllFriends(User user);

    Set<User> getCommonFriends(User user1, User user2);
}
