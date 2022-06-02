package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.IdFactory;
import ru.yandex.practicum.filmorate.utils.UserValidator;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();
    private final TreeSet<Long> userIdSet = new TreeSet<>();

    @PostMapping
    public User create(@RequestBody User user) {
        UserValidator.validateUser(user);
        UserValidator.validateUserCreation(users,user);
        IdFactory.setIdForUser(userIdSet,user);
        users.put(user.getId(), user);
        log.info("User is created: {}", user);
        return users.get(user.getId());
    }

    @PutMapping
    public User update(@RequestBody User user) {
        UserValidator.validateUser(user);
        UserValidator.validateUserUpdate(users, user);
        users.put(user.getId(),user);
        log.info("User is updated: {}", user);
        return users.get(user.getId());
    }

    @GetMapping
    public List<User> findAll() {
        log.info("Users are returned: {}", users.values());
        return new ArrayList<>(users.values());
    }

}
