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
        log.info("Users are returned: {}", userService.findAll());
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public User getById(@PathVariable(name = "id") Long id) throws DataNotFoundException {
        return userService.getById(id);
    }


    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable(name = "id") Long id, @PathVariable(name = "friendId") Long friendId)
            throws DataNotFoundException {
        return userService.addFriend(id, friendId);
    }


    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable(name = "id") Long id, @PathVariable(name = "friendId") Long friendId)
            throws DataNotFoundException {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable(name = "id") Long id) throws DataNotFoundException {
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(
            @PathVariable(name = "id") Long id,
            @PathVariable(name = "otherId") Long otherId) throws DataNotFoundException {
        return userService.getCommonFriends(id, otherId);
    }


}
