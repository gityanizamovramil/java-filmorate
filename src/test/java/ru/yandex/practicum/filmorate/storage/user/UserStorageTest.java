package ru.yandex.practicum.filmorate.storage.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("dev")
class UserStorageTest {

    private final UserStorage userStorage;

    @Autowired
    UserStorageTest(@Qualifier("UserDBStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @DirtiesContext
    @Test
    void create() throws ValidationException, DataNotFoundException {
        User user1 = new User("login","email@mail.com", LocalDate.of(2004,1,1));
        User user2 = new User("login","email@mail.com", LocalDate.of(2004,1,1));
        user2.setId(1L);
        user2.setName("login");
        userStorage.create(user1);
        User user3 = userStorage.getById(1L);
        assertEquals(user2.getId(),user3.getId());
        assertEquals(user2.getName(),user3.getName());
        assertEquals(user2.getLogin(),user3.getLogin());
        assertEquals(user2.getEmail(),user3.getEmail());
        assertEquals(user2.getBirthday(),user3.getBirthday());
    }

    @DirtiesContext
    @Test
    void update() throws ValidationException, DataNotFoundException {
        User user1 = new User("login","email@mail.com", LocalDate.of(2004,1,1));
        userStorage.create(user1);
        User user2 = new User("newLogin","newEmail@mail.com", LocalDate.of(2004,4,4));
        user2.setId(1L);
        user2.setName("name");
        userStorage.update(user2);
        User user3 = userStorage.getById(1L);
        assertEquals(user2.getId(),user3.getId());
        assertEquals(user2.getName(),user3.getName());
        assertEquals(user2.getLogin(),user3.getLogin());
        assertEquals(user2.getEmail(),user3.getEmail());
        assertEquals(user2.getBirthday(),user3.getBirthday());
    }

    @DirtiesContext
    @Test
    void getUserList() throws ValidationException {
        List<User> users = userStorage.getUserList();
        assertEquals(0,users.size());
        User user1 = new User("login","email@mail.com", LocalDate.of(2004,1,1));
        userStorage.create(user1);
        users = userStorage.getUserList();
        assertEquals(1,users.size());
    }

    @Test
    void getById() {
    }

    @Test
    void getFriends() {
    }

    @Test
    void addFriend() {
    }

    @Test
    void deleteFriend() {
    }
}