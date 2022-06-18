package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.TreeSet;


public class IdFactory {

    public static void setUserId(List<Long> idList, User user) {
        TreeSet<Long> idSet = new TreeSet<>(idList);
        if (idSet.isEmpty()) {
            user.setId(1L);
            return;
        }
        final Long last = idSet.last() + 1;
        user.setId(last);
    }

    public static void setFilmId(List<Long> idList, Film film) {
        TreeSet<Long> idSet = new TreeSet<>(idList);
        if (idSet.isEmpty()) {
            film.setId(1L);
            return;
        }
        final Long last = idSet.last() + 1;
        film.setId(last);
    }
}
