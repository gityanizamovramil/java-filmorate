package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.UserValidator;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Qualifier("UserDBStorage")
public class UserDBStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(User user) throws ValidationException {
        //Валидация user creation
        String sqlUserIdList = "SELECT USER_ID FROM USERS";
        List<Long> userIdList = jdbcTemplate.query(
                sqlUserIdList, (rs, rowNum) -> rs.getLong("USER_ID"));
        UserValidator.validateCreation(userIdList, user);

        UserValidator.validateUser(user);

        //Запись user в DB
        String sqlCreateUser = "INSERT INTO USERS(" +
                "LOGIN, " +
                "USER_NAME, " +
                "EMAIL, " +
                "BIRTHDAY) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlCreateUser, new String[]{"USER_ID"});
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void update(User user) throws ValidationException, DataNotFoundException {
        //Валидация user update
        String sqlUserIdList = "SELECT USER_ID FROM USERS";
        List<Long> userIdList = jdbcTemplate.query(
                sqlUserIdList, (rs, rowNum) -> rs.getLong("USER_ID"));
        UserValidator.validateUpdate(userIdList, user);

        UserValidator.validateUser(user);

        //Запись user в DB
        String sqlUpdateUser = "UPDATE USERS SET " +
                "LOGIN = ?, " +
                "USER_NAME = ?, " +
                "EMAIL = ?, " +
                "BIRTHDAY = ? " +
                "WHERE USER_ID = ?";

        jdbcTemplate.update(sqlUpdateUser
                , user.getLogin()
                , user.getName()
                , user.getEmail()
                , user.getBirthday()
                , user.getId());
    }

    @Override
    public List<User> getUserList() {
        //Возврат users
        String sqlUserById = "SELECT * FROM USERS";
        List<User> users = jdbcTemplate.query(sqlUserById, UserDBStorage::makeUser);
        return users;
    }

    @Override
    public User getById(Long id) throws DataNotFoundException {
        //Валидация user exist
        String sqlUserIdList = "SELECT USER_ID FROM USERS";
        List<Long> userIdList = jdbcTemplate.query(
                sqlUserIdList, (rs, rowNum) -> rs.getLong("USER_ID"));
        UserValidator.validateExist(userIdList, id);

        //Возврат user
        String sqlUserById = "SELECT * FROM USERS WHERE USER_ID = ?";
        List<User> users = jdbcTemplate.query(sqlUserById, UserDBStorage::makeUser, id);
        return users.get(0);
    }

    @Override
    public List<Long> getFriends(Long id) {
        //Возврат списка friends по user_id
        String sqlFriendsByUserId = "SELECT FRIEND_ID FROM USER_FRIENDS WHERE USER_ID = ?";
        List<Long> friends = jdbcTemplate.query(
                sqlFriendsByUserId, (rs, rowNum) -> rs.getLong("FRIEND_ID"), id);
        return friends;
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        //Запись friend в DB
        String sqlCreateFriendship = "INSERT INTO USER_FRIENDS(" +
                "USER_ID, " +
                "FRIEND_ID) " +
                "VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlCreateFriendship, new String[]{"FRIENDSHIP_ID"});
            stmt.setLong(1, id);
            stmt.setLong(2, friendId);
            return stmt;
        }, keyHolder);
        Long friendshipId = keyHolder.getKey().longValue();
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        //Удаление записи friend в DB
        String sqlDeleteFriendship = "DELETE FROM USER_FRIENDS " +
                "WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlDeleteFriendship, id, friendId);
    }

    static User makeUser(ResultSet rs, int RowNum) throws SQLException {
        //Создание user
        return new User(
                rs.getLong("USER_ID"),
                rs.getString("LOGIN"),
                rs.getString("USER_NAME"),
                rs.getString("EMAIL"),
                rs.getDate("BIRTHDAY").toLocalDate()
        );
    }
}
