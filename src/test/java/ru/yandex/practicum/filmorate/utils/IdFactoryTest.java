package ru.yandex.practicum.filmorate.utils;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class IdFactoryTest {

    @Test
    void setIdForUserWhenIdSetIsEmpty() {
        User user1 = new User("login1", "email1@mail.com", LocalDate.of(2004, 1, 1));
        List<Long> idSet = new ArrayList<>();
        IdFactory.setUserId(idSet, user1);
        assertEquals(0, idSet.size());
        assertEquals(1, user1.getId());
    }

    @Test
    void setIdForUserWhenIdSetIsNotEmpty() {
        User user1 = new User("login1", "email1@mail.com", LocalDate.of(2004, 1, 1));
        List<Long> idSet = new ArrayList<>();
        IdFactory.setUserId(idSet, user1);
        assertEquals(0, idSet.size());
        assertEquals(1, user1.getId());
        User user2 = new User("login2", "email2@mail.com", LocalDate.of(2004, 1, 2));
        assertEquals(0, idSet.size());
        idSet.add(1L);
        IdFactory.setUserId(idSet, user2);
        assertEquals(2, user2.getId());
    }

    @Test
    void setIdForFilmWhenIdSetIsEmpty() {
        Film film1 = new Film("name1", "description1", LocalDate.of(2022, 6, 1), 60);
        List<Long> idSet = new ArrayList<>();
        IdFactory.setFilmId(idSet, film1);
        assertEquals(0, idSet.size());
        assertEquals(1, film1.getId());
    }

    @Test
    void setIdForFilmWhenIdSetIsNotEmpty() {
        Film film1 = new Film("name1", "description1", LocalDate.of(2022, 6, 1), 60);
        List<Long> idSet = new ArrayList<>();
        IdFactory.setFilmId(idSet, film1);
        assertEquals(0, idSet.size());
        assertEquals(1, film1.getId());
        Film film2 = new Film("name2", "description2", LocalDate.of(2022, 6, 2), 60);
        assertEquals(0, idSet.size());
        idSet.add(1L);
        IdFactory.setFilmId(idSet, film2);
        assertEquals(2, film2.getId());
    }

}