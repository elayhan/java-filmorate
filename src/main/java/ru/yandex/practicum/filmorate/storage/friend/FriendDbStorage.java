package ru.yandex.practicum.filmorate.storage.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;

import java.util.HashSet;
import java.util.Set;

@Component
@Primary
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(User currentUser, User addingUser) {
        String insertFriendSQL = "INSERT INTO FRIEND (USER_ID,FRIEND_ID) VALUES (?,?)";
        try {
            jdbcTemplate.update(insertFriendSQL, currentUser.getId(), addingUser.getId());
        } catch (DuplicateKeyException ignored) {

        }
    }

    @Override
    public void removeFriend(User currentUser, User removeUser) {
        String removeFriendSQL = "DELETE FROM FRIEND WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(removeFriendSQL, currentUser.getId(), removeUser.getId());
    }

    @Override
    public Set<User> getAllFriends(User user) {
        String getAllFriendByUserSQL = "SELECT u.USER_ID, u.EMAIL, u.LOGIN , u.NAME , u.BIRTHDAY  FROM FRIEND f \n" +
                "JOIN USERS u ON f.FRIEND_ID = u.USER_ID \n" +
                "WHERE f.USER_ID = ?";

        return new HashSet<>(jdbcTemplate.query(getAllFriendByUserSQL, new UserRowMapper(), user.getId()));
    }

    @Override
    public Set<User> getCommonFriends(User user1, User user2) {
        String getCommonFriendSQL = "SELECT u.USER_ID, u.EMAIL, u.LOGIN , u.NAME , u.BIRTHDAY FROM FRIEND f\n" +
                "JOIN USERS u ON f.FRIEND_ID = u.USER_ID \n" +
                "WHERE f.USER_ID  = ?\n" +
                "AND EXISTS (SELECT 1 FROM FRIEND f2 WHERE FRIEND_ID = f.FRIEND_ID AND  USER_ID = ?)";
        return new HashSet<>(jdbcTemplate.query(getCommonFriendSQL, new UserRowMapper(), user1.getId(), user2.getId()));
    }
}
