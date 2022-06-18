package ru.yandex.practicum.filmorate.utils;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    void testWhenUserNameIsBlankChangedToLogin() throws ValidationException {
        User user = new User("login", "email@mail.ru", LocalDate.of(2004,1,1));
        user.setName("");
        UserValidator.validateUser(user);
        assertEquals("login", user.getName());
    }

    @Test
    void testWhenUserNameIsNullChangedToLogin() throws ValidationException {
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
        List<Long> users = new ArrayList<>();
        users.add(1L);
        ValidationException exception = assertThrows(ValidationException.class,
                ()-> UserValidator.validateCreation(users, user));
        assertEquals("User is already created", exception.getMessage());
    }

    @Test
    void validateUserUpdate() {
        User user = new User("login", "email@mail.ru", LocalDate.of(2004,1,1));
        user.setName("name");
        user.setId(1L);
        List<Long> users = new ArrayList<>();
        ValidationException exception = assertThrows(ValidationException.class,
                ()-> UserValidator.validateUpdate(users, user));
        assertEquals("User must be created firstly", exception.getMessage());
    }

}