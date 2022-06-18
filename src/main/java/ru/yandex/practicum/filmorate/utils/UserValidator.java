package ru.yandex.practicum.filmorate.utils;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Slf4j
public class UserValidator {

    public static void validateUser(User user) throws ValidationException {
        if (!StringUtils.hasText(user.getEmail()) || !user.getEmail().contains("@")) {
            log.error("User has incorrect email: {}", user);
            throw new ValidationException("Email of the user must be indicated");
        }
        if (!StringUtils.hasText(user.getLogin()) || StringUtils.containsWhitespace(user.getLogin())) {
            log.error("User has incorrect login: {}", user);
            throw new ValidationException("Login of the user must be indicated and have no whitespaces");
        }
        if (!StringUtils.hasText(user.getName())) {
            log.warn("User has blank or null name: {}", user);
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("User has incorrect birthday: {}", user);
            throw new ValidationException("Birthday must be less or equal to present time");
        }
    }

    public static void validateCreation(List<Long> userIdList, User user) throws ValidationException {
        if (user.getId() != null) {
            if (userIdList.contains(user.getId())) {
                throw new ValidationException("User is already created");
            }
        }
    }

    public static void validateUpdate(List<Long> userIdList, User user)
            throws ValidationException, DataNotFoundException {
        if(user.getId() == null) {
            log.error("Film has not been created yet: {}", user);
            throw new ValidationException("User must be created firstly");
        }
        if (!userIdList.contains(user.getId())) {
            log.error("Film has not been created yet: {}", user);
            throw new DataNotFoundException("User must be created firstly");
        }
    }

    public static void validateExist(List<Long> userIdList, Long id) throws DataNotFoundException {
        if(!userIdList.contains(id)) {
            throw new DataNotFoundException(String.format("User with %s is not found", id));
        }
    }
}
