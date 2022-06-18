package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) throws ValidationException {
        userStorage.create(user);
        return user;
    }

    public User update(User user) throws ValidationException, DataNotFoundException {
        userStorage.update(user);
        return user;
    }

    public User getById(Long id) throws DataNotFoundException {
        return userStorage.getById(id);
    }

    public User addFriend(Long id, Long friendId) throws DataNotFoundException {
        User userById = userStorage.getById(id);
        User friendById = userStorage.getById(friendId);
        userStorage.getUserList()
                .stream()
                .filter(user -> user.getId().equals(userById.getId()))
                .forEach(user -> user.getFriends().add(friendById.getId()));
        userStorage.getUserList()
                .stream()
                .filter(user -> user.getId().equals(friendById.getId()))
                .forEach(user -> user.getFriends().add(userById.getId()));
        return userById;
    }

    public User deleteFriend(Long id, Long friendId) throws DataNotFoundException {
        User userById = userStorage.getById(id);
        User friendById = userStorage.getById(friendId);
        userStorage.getUserList()
                .stream()
                .filter(user -> user.getFriends().contains(userById.getId()))
                .forEach(user -> user.getFriends().remove(friendById.getId()));
        userStorage.getUserList()
                .stream()
                .filter(user -> user.getFriends().contains(friendById.getId()))
                .forEach(user -> user.getFriends().remove(userById.getId()));
        return userById;
    }

    public List<User> getFriends(Long id) throws DataNotFoundException {
        User userById = userStorage.getById(id);
        return userStorage.getUserList()
                .stream()
                .filter(user -> user.getFriends().contains(id))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long id, Long otherId) throws DataNotFoundException {
        User userById = userStorage.getById(id);
        User otherUserById = userStorage.getById(otherId);
        List<Long> commonFriends = userStorage.getUserList()
                .stream()
                .filter(user -> user.getId().equals(userById.getId()))
                .findFirst()
                .orElseThrow()
                .getFriends()
                .stream()
                .filter(streamId -> otherUserById.getFriends().contains(streamId))
                .collect(Collectors.toList());
        List<User> userList = userStorage.getUserList();
        return userList.stream()
                .filter(user -> commonFriends.contains(user.getId()))
                .collect(Collectors.toList());
    }

    public List<User> findAll() {
        return userStorage.getUserList();
    }

}
