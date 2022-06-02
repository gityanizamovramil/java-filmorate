package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.FilmValidator;
import ru.yandex.practicum.filmorate.utils.IdFactory;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    private final TreeSet<Long> filmIdSet = new TreeSet<>();

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        FilmValidator.validateFilm(film);
        FilmValidator.validateFilmCreation(films, film);
        IdFactory.setIdForFilm(filmIdSet,film);
        films.put(film.getId(), film);
        log.info("Film is created: {}", film);
        return films.get(film.getId());
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
        FilmValidator.validateFilm(film);
        FilmValidator.validateFilmUpdate(films,film);
        films.put(film.getId(),film);
        log.info("Film is updated: {}", film);
        return films.get(film.getId());
    }

    @GetMapping
    public List<Film> findAll() {
        log.info("Films are returned: {}", films.values());
        return new ArrayList<>(films.values());
    }

}
