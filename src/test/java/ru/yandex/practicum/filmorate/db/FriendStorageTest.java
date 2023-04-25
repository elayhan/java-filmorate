package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendStorageTest {
    private final FriendDbStorage storage;
    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;

    User user1 = User.builder()
            .email("@@@1111@@@@")
            .login("sd")
            .name("ds")
            .birthday(LocalDate.of(2000, 1, 1))
            .build();

    User user2 = User.builder()
            .email("111@@@@1111")
            .login("ds")
            .name("sd")
            .birthday(LocalDate.of(1990, 1, 1))
            .build();

    @BeforeEach
    void deleteAllUsers() {
        jdbcTemplate.update("delete from users");
        jdbcTemplate.execute("alter table users alter column user_id restart with 1");
    }

    @Test
    void addFriendAndGetFriendsTest() {
        User currentUser = userDbStorage.createUser(user1);
        User friendUser = userDbStorage.createUser(user2);

        storage.addFriend(currentUser, friendUser);

        assertThat(storage.getAllFriends(currentUser)).isNotEmpty()
                .size().isEqualTo(1);
    }

    @Test
    void removeFriend() {
        User currentUser = userDbStorage.createUser(user1);
        User friendUser = userDbStorage.createUser(user2);

        storage.addFriend(currentUser, friendUser);

        assertThat(storage.getAllFriends(currentUser)).isNotEmpty()
                .size().isEqualTo(1);

        storage.removeFriend(currentUser, friendUser);
        assertThat(storage.getAllFriends(currentUser)).isEmpty();
    }

    @Test
    void getCommonFriends() {
        User currentUser = userDbStorage.createUser(user1);
        User friendUser = userDbStorage.createUser(user2);

        storage.addFriend(currentUser, friendUser);
        storage.addFriend(currentUser, friendUser);
        storage.addFriend(currentUser, currentUser);
        storage.addFriend(friendUser, currentUser);

        assertThat(storage.getCommonFriends(currentUser, friendUser))
                .isNotEmpty()
                .isEqualTo(Set.of(currentUser));
    }

}
