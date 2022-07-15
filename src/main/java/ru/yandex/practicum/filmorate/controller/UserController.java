package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.utils.UserValidator;

import java.util.*;

@RestController
@Slf4j
@RequestMapping
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

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
        log.info("Film with id {} is returned: {}", id, user);
        return user;
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public List<Long> addFriend(@PathVariable(name = "id") Long id, @PathVariable(name = "friendId") Long friendId)
            throws DataNotFoundException {
        List<Long> friends = userService.addFriend(id, friendId);
        log.info("Friend with friendId {} was added to user with id {}: {}", friendId, id, friends);
        return friends;
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public List<Long> deleteFriend(@PathVariable(name = "id") Long id, @PathVariable(name = "friendId") Long friendId)
            throws DataNotFoundException {
        List<Long> friends = userService.deleteFriend(id, friendId);
        log.info("Friend with friendId {} was deleted from user with id {}: {}", friendId, id, friends);
        return friends;
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable(name = "id") Long id) throws DataNotFoundException {
        List<User> users = userService.getFriends(id);
        log.info("Friends of user with id {} are returned: {}", id, users);
        return users;
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(
            @PathVariable(name = "id") Long id,
            @PathVariable(name = "otherId") Long otherId) throws DataNotFoundException {
        List<User> users = userService.getCommonFriends(id, otherId);
        log.info("Common friends of users with ids {} and {} are returned: {}", id, otherId, users);
        return users;
    }

}
