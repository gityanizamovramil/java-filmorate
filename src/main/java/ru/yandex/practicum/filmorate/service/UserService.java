package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public UserService(@Qualifier("UserDBStorage") UserStorage userStorage) {
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

    public List<Long> addFriend(Long id, Long friendId) throws DataNotFoundException {
        User userById = userStorage.getById(id);
        User friendById = userStorage.getById(friendId);
        List<Long> friends = userStorage.getFriends(userById.getId());
        if(!friends.contains(friendById.getId())) {
            userStorage.addFriend(userById.getId(),friendById.getId());
            friends = userStorage.getFriends(userById.getId());
        }
        return friends;
    }

    public List<Long> deleteFriend(Long id, Long friendId) throws DataNotFoundException {
        User userById = userStorage.getById(id);
        User friendById = userStorage.getById(friendId);
        List<Long> friends = userStorage.getFriends(userById.getId());
        if(friends.contains(friendById.getId())) {
            userStorage.deleteFriend(userById.getId(),friendById.getId());
            friends = userStorage.getFriends(userById.getId());
        }
        return friends;
    }

    public List<User> getFriends(Long id) throws DataNotFoundException {
        User userById = userStorage.getById(id);
        List<Long> friends = userStorage.getFriends(userById.getId());
        return userStorage.getUserList()
                .stream()
                .filter(user -> friends.contains(user.getId()))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long id, Long otherId) throws DataNotFoundException {
        User userById = userStorage.getById(id);
        User otherUserById = userStorage.getById(otherId);
        List<Long> userFriends = userStorage.getFriends(userById.getId());
        List<Long> otherUserFriends = userStorage.getFriends(otherUserById.getId());
        List<Long> commonFriends = userFriends
                .stream()
                .filter(otherUserFriends::contains)
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
