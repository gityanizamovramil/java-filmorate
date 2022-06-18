package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    void create(Film film) throws ValidationException;

    void update(Film film) throws ValidationException, DataNotFoundException;

    List<Film> getFilmList();

    Film getById(Long id) throws DataNotFoundException;

}
