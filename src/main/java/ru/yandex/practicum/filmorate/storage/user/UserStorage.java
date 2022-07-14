package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    void create(User user) throws ValidationException;

    void update(User user) throws ValidationException, DataNotFoundException;

    List<User> getUserList();

    User getById(Long id) throws DataNotFoundException;

    //новые методы
    List<Long> getFriends(Long id);

    void addFriend(Long id, Long friendId);

    void deleteFriend(Long id, Long friendId);
}
