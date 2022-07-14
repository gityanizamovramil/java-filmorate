package ru.yandex.practicum.filmorate.storage.film;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("dev")
class FilmStorageTest {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    FilmStorageTest(@Qualifier("FilmDBStorage") FilmStorage filmStorage
            , @Qualifier("UserDBStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @DirtiesContext
    @Test
    void create() throws DataNotFoundException, ValidationException {
        Film film1 = new Film("name1","description1", LocalDate.of(2022,6,1),60, new MPA(1,"G"));
        Film film2 = new Film("name1","description1", LocalDate.of(2022,6,1),60, new MPA(1,"G"));
        film2.setId(1L);
        filmStorage.create(film1);
        Film film3 = filmStorage.getById(1L);
        assertEquals(film2.getId(), film3.getId());
        assertEquals(film2.getName(), film3.getName());
        assertEquals(film2.getDescription(), film3.getDescription());
        assertEquals(film2.getReleaseDate(), film3.getReleaseDate());
        assertEquals(film2.getDuration(), film3.getDuration());
    }

    @DirtiesContext
    @Test
    void update() throws DataNotFoundException, ValidationException {
        Film film1 = new Film("name1","description1", LocalDate.of(2022,6,1),60, new MPA(1, "G"));
        filmStorage.create(film1);
        Film film2 = new Film("name2","description2", LocalDate.of(2022,6,2),120, new MPA(1, "G"));
        film2.setId(1L);
        filmStorage.update(film2);
        Film film3 = filmStorage.getById(1L);
        assertEquals(film2.getId(), film3.getId());
        assertEquals(film2.getName(), film3.getName());
        assertEquals(film2.getDescription(), film3.getDescription());
        assertEquals(film2.getReleaseDate(), film3.getReleaseDate());
        assertEquals(film2.getDuration(), film3.getDuration());
    }

    @DirtiesContext
    @Test
    void getFilmList() throws DataNotFoundException, ValidationException {
        List<Film> films = filmStorage.getFilmList();
        assertEquals(0,films.size());
        Film film1 = new Film("name1","description1", LocalDate.of(2022,6,1),60, new MPA(1, "G"));
        filmStorage.create(film1);
        films = filmStorage.getFilmList();
        assertEquals(1,films.size());
    }

    @Test
    void getById() {
    }

    @Test
    void addLike() {
    }

    @Test
    void deleteLike() {
    }

    @Test
    void getFilmLikes() {
    }

    @Test
    void getGenreList() {
    }

    @Test
    void getGenreById() {
    }

    @Test
    void getMpaList() {
    }

    @Test
    void getMpaByid() {
    }
}