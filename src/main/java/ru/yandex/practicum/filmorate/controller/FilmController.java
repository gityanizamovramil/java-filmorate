package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
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

    @GetMapping("/genres")
    public List<Genre> findAllGenres() {
        List<Genre> genres = filmService.findAllGenres();
        log.info("Genres are returned: {}", genres);
        return genres;
    }

    @GetMapping("/genres/{id}")
    public Genre findGenreById(@PathVariable(name="id") Integer id) throws DataNotFoundException {
        Genre genre = filmService.findGenreById(id);
        log.info(String.format("Genre with id %s is returned: {}", id), genre);
        return genre;
    }

    @GetMapping("/mpa")
    public List<MPA> findAllRatings() {
        List<MPA> ratings = filmService.findAllRatings();
        log.info("Ratings are returned: {}", ratings);
        return ratings;
    }

    @GetMapping("/mpa/{id}")
    public MPA findRatingById(@PathVariable(name="id") Integer id) throws DataNotFoundException {
        MPA mpa = filmService.findRatingById(id);
        log.info(String.format("MPA with id %s is returned: {}", id), mpa);
        return mpa;
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) throws ValidationException, DataNotFoundException {
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
    public List<Long> addLike(@PathVariable(name = "id") Long id, @PathVariable(name = "userId") Long userId)
            throws DataNotFoundException, ValidationException {
        List<Long> filmLikes = filmService.addLike(id, userId);
        log.info(String.format("User with id %s added like to the film with id %s: {}", userId, id), filmLikes);
        return filmLikes;
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public List<Long> deleteLike(@PathVariable(name = "id") Long id, @PathVariable(name = "userId") Long userId)
            throws DataNotFoundException {
        List<Long> filmLikes = filmService.deleteLike(id, userId);
        log.info(String.format("User with id %s deleted like from the film with id %s: {}", userId, id), filmLikes);
        return filmLikes;
    }

    @GetMapping("/films/popular")
    public List<Film> getPopular(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        List<Film> popular = filmService.getPopular(count);
        log.info("Popular films are returned: {}", popular);
        return popular;
    }

}
