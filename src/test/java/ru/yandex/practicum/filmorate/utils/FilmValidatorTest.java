package ru.yandex.practicum.filmorate.utils;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidatorTest {

    @Test
    void testValidationExceptionFilmNameIsIncorrect() {
        String incorrectName = "";
        Film film = new Film(incorrectName, "description", LocalDate.of(2022, 6, 1), 60);
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> FilmValidator.validateFilm(film)
        );
        assertEquals("Name of the film must be indicated", exception.getMessage());
    }

    @Test
    void testValidationExceptionFilmDescriptionIsTooLong() {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i <= 201; i++) {
            sb.append("a");
        }
        String tooLongDescription = sb.toString();
        Film film = new Film("name", tooLongDescription, LocalDate.of(2022, 6, 1), 60);
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> FilmValidator.validateFilm(film)
        );
        assertEquals("Description of the film must be less or equal than 200 chars", exception.getMessage());
    }

    @Test
    void testValidationExceptionFilmReleaseDateIsEarlier() {
        Film film = new Film("name", "description", LocalDate.of(1895, 12, 27), 60);
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> FilmValidator.validateFilm(film)
        );
        assertEquals("Release date of the film must be no earlier than 28.12.1895.", exception.getMessage());
    }

    @Test
    void testValidationExceptionFilmDurationIsNotPositive() {
        Film film = new Film("name", "description", LocalDate.of(2022, 6, 1), -60);
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> FilmValidator.validateFilm(film)
        );
        assertEquals("Duration of the film must be positive", exception.getMessage());
    }

    @Test
    void testValidationExceptionFilmIsCreatedBefore() {
        Film film = new Film("name", "description", LocalDate.of(2022, 6, 1), 60);
        film.setId(1L);
        Map<Long,Film> films = new HashMap<>();
        films.put(film.getId(),film);
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> FilmValidator.validateFilmCreation(films,film)
        );
        assertEquals("Film is already created", exception.getMessage());
    }
    @Test
    void testValidationExceptionFilmIsNotCreatedBefore() {
        Film film = new Film("name", "description", LocalDate.of(2022, 6, 1), 60);
        film.setId(1L);
        Map<Long,Film> films = new HashMap<>();
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> FilmValidator.validateFilmUpdate(films,film)
        );
        assertEquals("Film must be created firstly", exception.getMessage());
    }

}