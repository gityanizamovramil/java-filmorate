package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void create() throws ValidationException {
        User user1 = new User("login","email@mail.com", LocalDate.of(2004,1,1));
        User user2 = new User("login","email@mail.com", LocalDate.of(2004,1,1));
        user2.setId(1L);
        user2.setName("login");
        UserController userController = new UserController();
        User user3 = userController.create(user1);
        assertEquals(user2.getId(),user3.getId());
        assertEquals(user2.getName(),user3.getName());
        assertEquals(user2.getLogin(),user3.getLogin());
        assertEquals(user2.getEmail(),user3.getEmail());
        assertEquals(user2.getBirthday(),user3.getBirthday());
    }

    @Test
    void update() throws ValidationException {
        UserController userController = new UserController();
        User user1 = new User("login","email@mail.com", LocalDate.of(2004,1,1));
        userController.create(user1);
        User user2 = new User("newLogin","newEmail@mail.com", LocalDate.of(2004,4,4));
        user2.setId(1L);
        user2.setName("name");
        User user3 = userController.update(user2);
        assertEquals(user2.getId(),user3.getId());
        assertEquals(user2.getName(),user3.getName());
        assertEquals(user2.getLogin(),user3.getLogin());
        assertEquals(user2.getEmail(),user3.getEmail());
        assertEquals(user2.getBirthday(),user3.getBirthday());
    }

    @Test
    void findAll() throws ValidationException {
        UserController userController = new UserController();
        List<User> users = userController.findAll();
        assertEquals(0,users.size());
        User user1 = new User("login","email@mail.com", LocalDate.of(2004,1,1));
        userController.create(user1);
        users = userController.findAll();
        assertEquals(1,users.size());
    }
}