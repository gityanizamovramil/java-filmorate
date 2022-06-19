package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.utils.FilmValidator;

import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping
public class FilmController {

    private final FilmService filmService;

    @PostMapping("/films")
    public Film create(@RequestBody Film film) throws ValidationException {
        FilmValidator.validateFilm(film);
        filmService.create(film);
        log.info("Film is created: {}", film);
        return film;
    }

    @PutMapping("/films")
    public Film update(@RequestBody Film film) throws ValidationException, DataNotFoundException {
        FilmValidator.validateFilm(film);
        filmService.update(film);
        log.info("Film is updated: {}", film);
        return film;
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        List<Film> films = filmService.findAll();
        log.info("Films are returned: {}", films);
        return films;
    }

    @GetMapping("/films/{id}")
    public Film getById(@PathVariable(name="id") Long id) throws DataNotFoundException {
        Film film = filmService.getById(id);
        log.info(String.format("Film with id %s is returned: {}", id), film);
        return film;
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(@PathVariable(name = "id") Long id, @PathVariable(name = "userId") Long userId)
            throws DataNotFoundException {
        Film film = filmService.addLike(id, userId);
        log.info(String.format("User with id %s added like to the film with id %s: {}", userId, id), film);
        return film;
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLike(@PathVariable(name = "id") Long id, @PathVariable(name = "userId") Long userId)
            throws DataNotFoundException {
        Film film = filmService.deleteLike(id, userId);
        log.info(String.format("User with id %s deleted like from the film with id %s: {}", userId, id), film);
        return film;
    }

    @GetMapping("/films/popular")
    public List<Film> getPopular(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        List<Film> popular = filmService.getPopular(count);
        log.info("Popular films are returned: {}", popular);
        return popular;
    }

}
