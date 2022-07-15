package ru.yandex.practicum.filmorate.utils;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

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
        if (film.getMpa() == null || film.getMpa().getId() == null) {
            log.error("Film has incorrect MPA: {}", film);
            throw new ValidationException("MPA of the film must be indicated");
        }
    }

    public static void validateCreation(List<Long> filmIdList, Film film) throws ValidationException {
        if (film.getId() != null) {
            if (filmIdList.contains(film.getId())) {
                log.error("Film has been created before: {}", film);
                throw new ValidationException("Film is already created");
            }
        }
    }

    public static void validateUpdate(List<Long> filmIdList, Film film)
            throws ValidationException, DataNotFoundException {
        if (film.getId() == null) {
            log.error("Film has not been created yet: {}", film);
            throw new ValidationException("Film must be created firstly");
        }
        if (!filmIdList.contains(film.getId())) {
            log.error("Film has not been created yet: {}", film);
            throw new DataNotFoundException("Film must be created firstly");
        }
    }

    public static void validateExist(List<Long> filmIdList, Long id) throws DataNotFoundException {
        if (!filmIdList.contains(id)) {
            throw new DataNotFoundException(String.format("Film with %s is not found", id));
        }
    }

    public static void validateMPA(List<Integer> mpaIdList, Integer id) throws ValidationException {
        if (!mpaIdList.contains(id)) {
            throw new ValidationException(String.format("MPA with %s is not found", id));
        }
    }

    public static void validateExistMPA(List<MPA> mpaIdList, Integer id) throws DataNotFoundException {
        if (mpaIdList.isEmpty() || mpaIdList.get(0) == null || !mpaIdList.get(0).getId().equals(id)) {
            throw new DataNotFoundException(String.format("MPA with %s is not found", id));
        }
    }

    public static void validateGenre(List<Integer> genreList, Integer id) throws ValidationException {
        if (!genreList.contains(id)) {
            throw new ValidationException(String.format("Genre with %s is not found", id));
        }
    }

    public static void validateExistGenre(List<Genre> genreList, Integer id) throws DataNotFoundException {
        if (genreList.isEmpty() || genreList.get(0) == null || !genreList.get(0).getId().equals(id)) {
            throw new DataNotFoundException(String.format("Genre with %s is not found", id));
        }
    }

    public static void validateLike(List<Long> userLikes, Long id) throws ValidationException {
        if (userLikes.contains(id)) {
            log.error("Film has been liked by user before: {}", userLikes);
            throw new ValidationException("Film is already liked by user");
        }

    }
}
