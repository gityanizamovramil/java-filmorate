package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.FilmValidator;
import ru.yandex.practicum.filmorate.utils.IdFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
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

}
















