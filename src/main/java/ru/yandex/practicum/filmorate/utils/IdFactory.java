package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.TreeSet;

public class IdFactory {

    public static void setIdForUser(TreeSet<Long> idSet, User user) {
        if (idSet.isEmpty()) {
            idSet.add(1L);
            user.setId(1L);
            return;
        }
        final Long last = idSet.last() + 1;
        idSet.add(last);
        user.setId(last);
    }

    public static void setIdForFilm(TreeSet<Long> idSet, Film film) {
        if (idSet.isEmpty()) {
            idSet.add(1L);
            film.setId(1L);
            return;
        }
        final Long last = idSet.last() + 1;
        idSet.add(last);
        film.setId(last);
    }
}
