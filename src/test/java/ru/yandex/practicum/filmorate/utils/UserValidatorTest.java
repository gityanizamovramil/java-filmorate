package ru.yandex.practicum.filmorate.utils;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    @Test
    void testValidationExceptionUserEmailIsBlank() {
        User user = new User("login", " ", LocalDate.of(2004,1,1));
        ValidationException exception = assertThrows(ValidationException.class,
                ()-> UserValidator.validateUser(user));
        assertEquals("Email of the user must be indicated", exception.getMessage());
    }

    @Test
    void testValidationExceptionUserEmailDoesNotContainCharAt() {
        User user = new User("login", "email.mail.ru", LocalDate.of(2004,1,1));
        ValidationException exception = assertThrows(ValidationException.class,
                ()-> UserValidator.validateUser(user));
        assertEquals("Email of the user must be indicated", exception.getMessage());
    }

    @Test
    void testValidationExceptionUserLoginIsBlank() {
        User user = new User("", "email@mail.ru", LocalDate.of(2004,1,1));
        ValidationException exception = assertThrows(ValidationException.class,
                ()-> UserValidator.validateUser(user));
        assertEquals("Login of the user must be indicated and have no whitespaces", exception.getMessage());
    }

    @Test
    void testValidationExceptionUserLoginHasWhitespace() {
        User user = new User("log in", "email@mail.ru", LocalDate.of(2004,1,1));
        ValidationException exception = assertThrows(ValidationException.class,
                ()-> UserValidator.validateUser(user));
        assertEquals("Login of the user must be indicated and have no whitespaces", exception.getMessage());
    }

    @Test
    void testWhenUserNameIsBlankChangedToLogin() {
        User user = new User("login", "email@mail.ru", LocalDate.of(2004,1,1));
        user.setName("");
        UserValidator.validateUser(user);
        assertEquals("login", user.getName());
    }

    @Test
    void testWhenUserNameIsNullChangedToLogin() {
        User user = new User("login", "email@mail.ru", LocalDate.of(2004,1,1));
        UserValidator.validateUser(user);
        assertEquals("login", user.getName());
    }

    @Test
    void testValidationExceptionUserBirthdayIsInFuture() {
        User user = new User("login", "email@mail.ru", LocalDate.of(3022,1,1));
        ValidationException exception = assertThrows(ValidationException.class,
                ()-> UserValidator.validateUser(user));
        assertEquals("Birthday must be less or equal to present time", exception.getMessage());
    }

    @Test
    void testValidationExceptionUserIsCreatedBefore() {
        User user = new User("login", "email@mail.ru", LocalDate.of(2004,1,1));
        user.setName("name");
        user.setId(1L);
        Map<Long, User> users = new HashMap<>();
        users.put(1L, user);
        ValidationException exception = assertThrows(ValidationException.class,
                ()-> UserValidator.validateUserCreation(users, user));
        assertEquals("User is already created", exception.getMessage());
    }

    @Test
    void validateUserUpdate() {
        User user = new User("login", "email@mail.ru", LocalDate.of(2004,1,1));
        user.setName("name");
        user.setId(1L);
        Map<Long, User> users = new HashMap<>();
        ValidationException exception = assertThrows(ValidationException.class,
                ()-> UserValidator.validateUserUpdate(users, user));
        assertEquals("User must be created firstly", exception.getMessage());
    }

}