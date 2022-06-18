package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    @Test
    void create() throws ValidationException {
        Film film1 = new Film("name1","description1", LocalDate.of(2022,6,1),60);
        Film film2 = new Film("name1","description1", LocalDate.of(2022,6,1),60);
        film2.setId(1L);
        FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage()));
        Film film3 = filmController.create(film1);
        assertEquals(film2.getId(), film3.getId());
        assertEquals(film2.getName(), film3.getName());
        assertEquals(film2.getDescription(), film3.getDescription());
        assertEquals(film2.getReleaseDate(), film3.getReleaseDate());
        assertEquals(film2.getDuration(), film3.getDuration());
    }

    @Test
    void update() throws ValidationException, DataNotFoundException {
        FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage()));
        Film film1 = new Film("name1","description1", LocalDate.of(2022,6,1),60);
        filmController.create(film1);
        Film film2 = new Film("name2","description2", LocalDate.of(2022,6,2),120);
        film2.setId(1L);
        Film film3 = filmController.update(film2);
        assertEquals(film2.getId(), film3.getId());
        assertEquals(film2.getName(), film3.getName());
        assertEquals(film2.getDescription(), film3.getDescription());
        assertEquals(film2.getReleaseDate(), film3.getReleaseDate());
        assertEquals(film2.getDuration(), film3.getDuration());
    }

    @Test
    void findAll() throws ValidationException {
        FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage()));
        List<Film> films = filmController.findAll();
        assertEquals(0,films.size());
        Film film1 = new Film("name1","description1", LocalDate.of(2022,6,1),60);
        filmController.create(film1);
        films = filmController.findAll();
        assertEquals(1,films.size());
    }
}