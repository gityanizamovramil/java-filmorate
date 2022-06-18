package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.utils.FilmValidator;

import java.util.*;

@RestController
@Slf4j
@RequestMapping
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

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
        log.info("Films are returned: {}", filmService.findAll());
        return filmService.findAll();
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(@PathVariable(name = "id") Long id, @PathVariable(name = "userId") Long userId)
            throws DataNotFoundException {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLike(@PathVariable(name = "id") Long id, @PathVariable(name = "userId") Long userId)
            throws DataNotFoundException {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/{id}")
    public Film getById(@PathVariable(name="id") Long id) throws DataNotFoundException {
        return filmService.getById(id);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopular(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        log.info("Films are returned: {}", filmService.getPopular(count));
        return filmService.getPopular(count);
    }
}
