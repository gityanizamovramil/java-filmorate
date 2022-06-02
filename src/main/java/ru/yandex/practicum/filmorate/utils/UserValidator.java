package ru.yandex.practicum.filmorate.utils;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeSet;

@Slf4j
public class UserValidator {
    public static void validateUser(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("User has incorrect email: {}", user);
            throw new ValidationException("Email of the user must be indicated");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("User has incorrect login: {}", user);
            throw new ValidationException("Login of the user must be indicated and have no whitespaces");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("User has blank or null name: {}", user);
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("User has incorrect birthday: {}", user);
            throw new ValidationException("Birthday must be less or equal to present time");
        }
    }

    public static void validateUserCreation(Map<Long, User> users, User user) {
        if (users.containsKey(user.getId())) {
            throw new ValidationException("User is already created");
        }
    }

    public static void validateUserUpdate(Map<Long, User> users, User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Film has not been created yet: {}", user);
            throw new ValidationException("User must be created firstly");
        }
    }
}
