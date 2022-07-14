package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.IdFactory;
import ru.yandex.practicum.filmorate.utils.UserValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public void create(User user) throws ValidationException {
        UserValidator.validateCreation(new ArrayList<>(users.keySet()) ,user);
        IdFactory.setUserId(new ArrayList<>(users.keySet()),user);
        users.put(user.getId(),user);
    }

    @Override
    public void update(User user) throws ValidationException, DataNotFoundException {
        UserValidator.validateUpdate(new ArrayList<>(users.keySet()), user);
        users.put(user.getId(),user);
    }

    @Override
    public List<User> getUserList() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getById(Long id) throws DataNotFoundException {
        UserValidator.validateExist(new ArrayList<>(users.keySet()), id);
        return users.get(id);
    }

    //новые методы
    @Override
    public List<Long> getFriends(Long id) {
        return null;
    }

    @Override
    public void addFriend(Long id, Long friendId) {

    }

    @Override
    public void deleteFriend(Long id, Long friendId) {

    }
}
