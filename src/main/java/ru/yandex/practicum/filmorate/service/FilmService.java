package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.utils.FilmValidator;
import ru.yandex.practicum.filmorate.utils.UserValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDBStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Genre> findAllGenres() {
        return filmStorage.getGenreList();
    }

    public Genre findGenreById(Integer id) throws DataNotFoundException {
        return filmStorage.getGenreById(id);
    }

    public List<MPA> findAllRatings() {
        return filmStorage.getMpaList();
    }

    public MPA findRatingById(Integer id) throws DataNotFoundException {
        return filmStorage.getMpaByid(id);
    }

    public Film create(Film film) throws ValidationException, DataNotFoundException {
        filmStorage.create(film);
        return film;
    }

    public Film update(Film film) throws ValidationException, DataNotFoundException {
        filmStorage.update(film);
        return film;
    }

    public List<Film> findAll() {
        return filmStorage.getFilmList();
    }

    public List<Long> addLike(Long id, Long userId) throws DataNotFoundException, ValidationException {
        Film film = filmStorage.getById(id);
        filmStorage.addLike(film.getId(), userId);
        return filmStorage.getFilmLikes(film.getId());
    }

    public List<Long> deleteLike(Long id, Long userId) throws DataNotFoundException {
        Film film = filmStorage.getById(id);
        filmStorage.deleteLike(film.getId(), userId);
        return filmStorage.getFilmLikes(film.getId());
    }

    public List<Film> getPopular(Integer count) {
        List<Film> popular = filmStorage.getFilmList();
        if (popular.size() <= 1) {
            return popular;
        } else {
            return popular.stream()
                    .sorted((film1, film2) -> {
                        int result =
                                Integer.compare(
                                        filmStorage.getFilmLikes(film1.getId()).size()
                                        , filmStorage.getFilmLikes(film2.getId()).size());
                        result = -1 * result;
                        return result;
                    })
                    .limit(count)
                    .collect(Collectors.toList());
        }
    }

    public Film getById(Long id) throws DataNotFoundException {
        return filmStorage.getById(id);
    }
}










