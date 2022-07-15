package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.utils.FilmValidator;
import ru.yandex.practicum.filmorate.utils.UserValidator;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Qualifier("FilmDBStorage")
public class FilmDBStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenreList() {
        String sqlGenres = "SELECT * FROM GENRES";
        List<Genre> allGenres = jdbcTemplate.query(sqlGenres, FilmDBStorage::makeGenre);
        return allGenres;
    }

    @Override
    public Genre getGenreById(Integer id) throws DataNotFoundException {
        String sqlGenres = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        List<Genre> genresById = jdbcTemplate.query(sqlGenres, FilmDBStorage::makeGenre, id);
        FilmValidator.validateExistGenre(genresById, id);
        return genresById.get(0);
    }

    @Override
    public List<MPA> getMpaList() {
        String sqlMpa = "SELECT * FROM RATINGS";
        List<MPA> allMpa = jdbcTemplate.query(sqlMpa, FilmDBStorage::makeMpa);
        return allMpa;
    }

    @Override
    public MPA getMpaById(Integer id) throws DataNotFoundException {
        String sqlMpa = "SELECT * FROM RATINGS WHERE RATING_ID = ?";
        List<MPA> mpaById = jdbcTemplate.query(sqlMpa, FilmDBStorage::makeMpa, id);
        FilmValidator.validateExistMPA(mpaById, id);
        return mpaById.get(0);
    }

    @Override
    public void addLike(Long id, Long userId) throws ValidationException {
        //Валидация user_id
        String sqlFilmLikesById = "SELECT * FROM FILM_USER_LIKES WHERE FILM_ID = ?";
        List<Long> filmLikesList = jdbcTemplate.query(sqlFilmLikesById, (rs, rowNum) -> rs.getLong("USER_ID"), id);
        FilmValidator.validateLike(filmLikesList, userId);

        //Запись like в DB
        String sqlCreateLike =
                "INSERT INTO FILM_USER_LIKES(" +
                        "FILM_ID, " +
                        "USER_ID) " +
                        "VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlCreateLike, new String[]{"LIKE_ID"});
            stmt.setLong(1, id);
            stmt.setLong(2, userId);
            return stmt;
        }, keyHolder);
        Long likeId = keyHolder.getKey().longValue();
    }

    @Override
    public void deleteLike(Long id, Long userId) throws DataNotFoundException {
        //Валидация user_id
        String sqlFilmLikesById = "SELECT * FROM FILM_USER_LIKES WHERE FILM_ID = ?";
        List<Long> filmLikesList = jdbcTemplate.query(sqlFilmLikesById, (rs, rowNum) -> rs.getLong("USER_ID"), id);
        UserValidator.validateExist(filmLikesList, userId);

        //Удаление записи like в DB
        String sqlDeleteLike = "DELETE FROM FILM_USER_LIKES " +
                "WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlDeleteLike, id, userId);
    }

    @Override
    public List<Long> getFilmLikes(Long id) {
        //Возврат likes
        String sqlFilmLikesById = "SELECT * FROM FILM_USER_LIKES WHERE FILM_ID = ?";
        List<Long> filmLikesList = jdbcTemplate.query(sqlFilmLikesById, (rs, rowNum) -> rs.getLong("USER_ID"), id);
        return filmLikesList;
    }

    @Override
    public void create(Film film) throws ValidationException, DataNotFoundException {
        //Валидация film
        FilmValidator.validateFilm(film);

        //Валидация film (create)
        String sqlFilmIdList = "SELECT FILM_ID FROM FILMS";
        List<Long> filmIdList = jdbcTemplate.query(
                sqlFilmIdList, (rs, rowNum) -> rs.getLong("FILM_ID"));
        FilmValidator.validateCreation(filmIdList, film);

        //Валидация mpa
        Integer mpaId = film.getMpa().getId();
        String sqlMpa = "SELECT * FROM RATINGS WHERE RATING_ID = ?";
        List<MPA> mpaById = jdbcTemplate.query(sqlMpa, FilmDBStorage::makeMpa, mpaId);
        FilmValidator.validateExistMPA(mpaById, mpaId);
        MPA mpa = mpaById.get(0);

        //Валидация genre
        Set<Integer> genreSet = film.getGenres()
                .stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());
        List<Genre> filmGenres = new ArrayList<>();
        for (Integer id : genreSet) {
            String sqlGenresId = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
            List<Genre> genresById = jdbcTemplate.query(
                    sqlGenresId, FilmDBStorage::makeGenre, id);
            FilmValidator.validateExistGenre(genresById, id);
            filmGenres.add(genresById.get(0));
        }

        //Доведение film к модели
        film.setGenres(filmGenres);
        film.setMpa(mpa);

        //Запись film в DB
        String sqlCreateFilm = "INSERT INTO FILMS(" +
                "FILM_NAME, " +
                "DESCRIPTION, " +
                "RELEASE_DATE, " +
                "DURATION, " +
                "RATING) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlCreateFilm, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        Long filmId = keyHolder.getKey().longValue();
        film.setId(filmId);

        //Запись film_genres в DB
        String sqlCreateFilmGenre = "INSERT INTO FILM_GENRES(" +
                "FILM_ID, " +
                "GENRE_ID) " +
                "VALUES (?, ?)";
        for (Integer genreId : genreSet) {
            jdbcTemplate.update(sqlCreateFilmGenre
                    , film.getId()
                    , genreId
            );
        }
    }

    @Override
    public void update(Film film) throws ValidationException, DataNotFoundException {
        //Валидация film
        FilmValidator.validateFilm(film);

        //Валидация film (update)
        String sqlFilmIdList = "SELECT FILM_ID FROM FILMS";
        List<Long> filmIdList = jdbcTemplate.query(
                sqlFilmIdList, (rs, rowNum) -> rs.getLong("FILM_ID"));
        FilmValidator.validateUpdate(filmIdList, film);

        //Валидация mpa
        Integer mpaId = film.getMpa().getId();
        String sqlMpa = "SELECT * FROM RATINGS WHERE RATING_ID = ?";
        List<MPA> mpaById = jdbcTemplate.query(sqlMpa, FilmDBStorage::makeMpa, mpaId);
        FilmValidator.validateExistMPA(mpaById, mpaId);
        MPA mpa = mpaById.get(0);

        //Валидация genre
        Set<Integer> genreSet = film.getGenres()
                .stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());
        List<Genre> filmGenres = new ArrayList<>();
        for (Integer id : genreSet) {
            String sqlGenresId = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
            List<Genre> genresById = jdbcTemplate.query(
                    sqlGenresId, FilmDBStorage::makeGenre, id);
            FilmValidator.validateExistGenre(genresById, id);
            filmGenres.add(genresById.get(0));
        }

        //Доведение film к модели
        film.setGenres(filmGenres);
        film.setMpa(mpa);

        //Запись film в DB
        String sqlUpdateFilm = "UPDATE FILMS SET " +
                "FILM_NAME = ?, " +
                "DESCRIPTION = ?, " +
                "RELEASE_DATE = ?, " +
                "DURATION = ?, " +
                "RATING = ? " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlUpdateFilm
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId());

        //Удаление записи film_genre в DB
        String sqlDeleteFilmGenre = "DELETE FROM FILM_GENRES " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlDeleteFilmGenre, film.getId());

        //Запись film_genres в DB
        String sqlCreateFilmGenre = "INSERT INTO FILM_GENRES(" +
                "FILM_ID, " +
                "GENRE_ID) " +
                "VALUES (?, ?)";
        for (Integer genreId : genreSet) {
            jdbcTemplate.update(sqlCreateFilmGenre
                    , film.getId()
                    , genreId
            );
        }
    }

    @Override
    public List<Film> getFilmList() {
        //Создание film с пустым списком жанров
        String sqlFilmById = "SELECT * FROM FILMS LEFT OUTER JOIN RATINGS ON FILMS.RATING = RATINGS.RATING_ID";
        List<Film> films = jdbcTemplate.query(sqlFilmById, FilmDBStorage::makeFilm);

        for (Film film : films) {
            //Создание списка жанров для конкретного film
            String sqlFilmGenres = "SELECT * " +
                    "FROM FILM_GENRES LEFT OUTER JOIN GENRES " +
                    "ON GENRES.GENRE_ID = FILM_GENRES.GENRE_ID " +
                    "WHERE FILM_ID = ?";
            List<Genre> filmGenres = jdbcTemplate.query(
                    sqlFilmGenres, FilmDBStorage::makeGenre, film.getId());
            //Установка жанров для film
            film.setGenres(filmGenres);
        }

        //Вовзрат списка готовых film с жанрами
        return films;
    }

    @Override
    public Film getById(Long id) throws DataNotFoundException {
        //Валидация film exist
        String sqlFilmIdList = "SELECT FILM_ID FROM FILMS";
        List<Long> filmIdList = jdbcTemplate.query(
                sqlFilmIdList, (rs, rowNum) -> rs.getLong("FILM_ID"));
        FilmValidator.validateExist(filmIdList, id);

        //Создание film с пустым списком жанров
        String sqlFilmById = "SELECT * " +
                "FROM FILMS LEFT OUTER JOIN RATINGS " +
                "ON FILMS.RATING = RATINGS.RATING_ID " +
                "WHERE FILM_ID = ?";
        List<Film> films = jdbcTemplate.query(sqlFilmById, FilmDBStorage::makeFilm, id);
        Film film = films.get(0);

        //Создание списка жанров для конкретного film
        String sqlFilmGenres = "SELECT * " +
                "FROM FILM_GENRES LEFT OUTER JOIN GENRES " +
                "ON GENRES.GENRE_ID = FILM_GENRES.GENRE_ID " +
                "WHERE FILM_ID = ?";
        List<Genre> filmGenres = jdbcTemplate.query(
                sqlFilmGenres, FilmDBStorage::makeGenre, film.getId());

        //Установка жанров для film
        film.setGenres(filmGenres);

        //Вовзрат готового film со списком жанров
        return film;
    }

    static MPA makeMpa(ResultSet rs, int rowNum) throws SQLException {
        //Создание mpa
        return new MPA(
                rs.getInt("RATING_ID"),
                rs.getString("RATING_NAME")
        );
    }

    static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(
                rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME")
        );
    }

    static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        //Создание film
        Film film = new Film(
                rs.getString("FILM_NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION"),
                new MPA(rs.getInt("RATING"), rs.getString("RATING_NAME"))
        );
        film.setId(rs.getLong("FILM_ID"));
        return film;
    }


}
