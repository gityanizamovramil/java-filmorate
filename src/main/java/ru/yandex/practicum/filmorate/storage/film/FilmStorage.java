package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface FilmStorage {

    void create(Film film) throws ValidationException, DataNotFoundException;

    void update(Film film) throws ValidationException, DataNotFoundException;

    List<Film> getFilmList();

    Film getById(Long id) throws DataNotFoundException;

    //новые методы
    void addLike(Long id, Long userId) throws ValidationException;

    void deleteLike(Long id, Long userId) throws DataNotFoundException;

    List<Long> getFilmLikes(Long id);

    List<Genre> getGenreList();

    Genre getGenreById(Integer id) throws DataNotFoundException;

    List<MPA> getMpaList();

    MPA getMpaById(Integer id) throws DataNotFoundException;
}
