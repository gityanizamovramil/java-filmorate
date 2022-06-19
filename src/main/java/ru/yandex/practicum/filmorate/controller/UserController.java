package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.utils.UserValidator;

import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public User create(@RequestBody User user) throws ValidationException {
        UserValidator.validateUser(user);
        userService.create(user);
        log.info("User is created: {}", user);
        return user;
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) throws ValidationException, DataNotFoundException {
        UserValidator.validateUser(user);
        userService.update(user);
        log.info("User is updated: {}", user);
        return user;
    }

    @GetMapping("/users")
    public List<User> findAll() {
        List<User> users = userService.findAll();
        log.info("Users are returned: {}", users);
        return users;
    }

    @GetMapping("/users/{id}")
    public User getById(@PathVariable(name = "id") Long id) throws DataNotFoundException {
        User user = userService.getById(id);
        log.info(String.format("Film with id %s is returned: {}", id), user);
        return user;
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable(name = "id") Long id, @PathVariable(name = "friendId") Long friendId)
            throws DataNotFoundException {
        User user = userService.addFriend(id, friendId);
        log.info(String.format("Friend with friendId %s was added to user with id %s: {}", friendId, id), user);
        return user;
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable(name = "id") Long id, @PathVariable(name = "friendId") Long friendId)
            throws DataNotFoundException {
        User user = userService.deleteFriend(id, friendId);
        log.info(String.format("Friend with friendId %s was deleted from user with id %s: {}", friendId, id), user);
        return user;
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable(name = "id") Long id) throws DataNotFoundException {
        List<User> users = userService.getFriends(id);
        log.info(String.format("Friends of user with id %s are returned: {}", id), users);
        return users;
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(
            @PathVariable(name = "id") Long id,
            @PathVariable(name = "otherId") Long otherId) throws DataNotFoundException {
        List<User> users = userService.getCommonFriends(id, otherId);
        log.info(String.format("Common friends of users with ids %s and %s are returned: {}", id, otherId), users);
        return users;
    }

}
