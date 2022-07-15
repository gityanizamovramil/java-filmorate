package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.utils.FilmValidator;
import ru.yandex.practicum.filmorate.utils.IdFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public void create(Film film) throws ValidationException {
        FilmValidator.validateCreation(new ArrayList<>(films.keySet()), film);
        IdFactory.setFilmId(new ArrayList<>(films.keySet()), film);
        films.put(film.getId(), film);
    }

    @Override
    public void update(Film film) throws ValidationException, DataNotFoundException {
        FilmValidator.validateUpdate(new ArrayList<>(films.keySet()), film);
        films.put(film.getId(), film);
    }

    @Override
    public List<Film> getFilmList() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getById(Long id) throws DataNotFoundException {
        FilmValidator.validateExist(new ArrayList<>(films.keySet()), id);
        return films.get(id);
    }

    //новые методы
    @Override
    public void addLike(Long id, Long userId) {

    }

    @Override
    public void deleteLike(Long id, Long userId) {

    }

    @Override
    public List<Long> getFilmLikes(Long id) {
        return null;
    }

    @Override
    public List<Genre> getGenreList() {
        return null;
    }

    @Override
    public Genre getGenreById(Integer id) {
        return null;
    }

    @Override
    public List<MPA> getMpaList() {
        return null;
    }

    @Override
    public MPA getMpaById(Integer id) {
        return null;
    }
}
















