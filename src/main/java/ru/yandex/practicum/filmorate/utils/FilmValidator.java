package ru.yandex.practicum.filmorate.utils;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

@Slf4j
public class FilmValidator {
    public static void validateFilm(Film film) throws ValidationException {
        if (StringUtils.isBlank(film.getName())) {
            log.error("Film has incorrect name: {}", film);
            throw new ValidationException("Name of the film must be indicated");
        }
        if (StringUtils.defaultString(film.getDescription()).length() > 200) {
            log.error("Film has incorrect length of description: {}", film);
            throw new ValidationException("Description of the film must be less or equal than 200 chars");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.error("Film has incorrect length of description: {}", film);
            throw new ValidationException("Release date of the film must be no earlier than 28.12.1895.");
        }
        if (film.getDuration() <= 0) {
            log.error("Film has negative duration: {}", film);
            throw new ValidationException("Duration of the film must be positive");
        }
    }

    public static void validateFilmCreation(Map<Long, Film> films, Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            log.error("Film has been created before: {}", film);
            throw new ValidationException("Film is already created");
        }
    }

    public static void validateFilmUpdate(Map<Long, Film> films, Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            log.error("Film has not been created yet: {}", film);
            throw new ValidationException("Film must be created firstly");
        }
    }
}
