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
        String sqlGenres = "SELECT * FROM CATEGORIES";
        List<Genre> allGenres = jdbcTemplate.query(sqlGenres, FilmDBStorage::makeGenre);
        return allGenres;
    }

    @Override
    public Genre getGenreById(Integer id) throws DataNotFoundException {
        String sqlGenreIdList = "SELECT CATEGORY_ID FROM CATEGORIES";
        List<Integer> genreIdList = jdbcTemplate.query(
                sqlGenreIdList, (rs, rowNum) -> rs.getInt("CATEGORY_ID"));
        FilmValidator.validateExistGenre(genreIdList, id);

        String sqlGenres = "SELECT * FROM CATEGORIES";
        List<Genre> allGenres = jdbcTemplate.query(sqlGenres, FilmDBStorage::makeGenre);
        List<Genre> genresById = allGenres
                .stream()
                .filter(genre -> genre.getId().equals(id))
                .collect(Collectors.toList());
        return genresById.get(0);
    }

    @Override
    public List<MPA> getMpaList() {
        String sqlMpa = "SELECT * FROM RATINGS";
        List<MPA> allMpa = jdbcTemplate.query(sqlMpa, FilmDBStorage::makeMpa);
        return allMpa;
    }

    @Override
    public MPA getMpaByid(Integer id) throws DataNotFoundException {
        String sqlMpaIdList = "SELECT RATING_ID FROM RATINGS";
        List<Integer> mpaIdList = jdbcTemplate.query(
                sqlMpaIdList, (rs, rowNum) -> rs.getInt("RATING_ID"));
        FilmValidator.validateExistMPA(mpaIdList, id);

        String sqlMpa = "SELECT * FROM RATINGS";
        List<MPA> allMpa = jdbcTemplate.query(sqlMpa, FilmDBStorage::makeMpa);
        List<MPA> mpaById = allMpa
                .stream()
                .filter(mpa -> mpa.getId().equals(id))
                .collect(Collectors.toList());
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
        //Валидация film (create)
        String sqlFilmIdList = "SELECT FILM_ID FROM FILMS";
        List<Long> filmIdList = jdbcTemplate.query(
                sqlFilmIdList, (rs, rowNum) -> rs.getLong("FILM_ID"));
        FilmValidator.validateCreation(filmIdList, film);

        //Валидация mpa
        MPA mpa = film.getMpa();
        Integer mpaId = mpa.getId();
        String sqlMpaIdList = "SELECT RATING_ID FROM RATINGS";
        List<Integer> mpaIdList = jdbcTemplate.query(
                sqlMpaIdList, (rs, rowNum) -> rs.getInt("RATING_ID"));
        FilmValidator.validateMPA(mpaIdList, mpaId);

        //Валидация genre
        Set<Integer> genreSet = film.getGenres()
                .stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());
        String sqlGenreIdList = "SELECT CATEGORY_ID FROM CATEGORIES";
        List<Integer> genreIdList = jdbcTemplate.query(
                sqlGenreIdList, (rs, rowNum) -> rs.getInt("CATEGORY_ID"));
        for (Integer id : genreSet) {
            FilmValidator.validateGenre(genreIdList, id);
        }

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
        String sqlCreateFilmGenre = "INSERT INTO FILM_CATEGORIES(" +
                "FILM_ID, " +
                "CATEGORY_ID) " +
                "VALUES (?, ?)";
        for (Integer genreId : genreSet) {
            jdbcTemplate.update(sqlCreateFilmGenre
                    , film.getId()
                    , genreId
            );
        }

        //Доведение film к модели
        String sqlGenres = "SELECT * FROM CATEGORIES";
        List<Genre> allGenres = jdbcTemplate.query(sqlGenres, FilmDBStorage::makeGenre);
        List<Genre> filmGenres = allGenres
                .stream()
                .filter(genre -> genreSet.contains(genre.getId()))
                .collect(Collectors.toList());
        film.setGenres(filmGenres);
        String sqlMpa = "SELECT * FROM RATINGS WHERE RATING_ID = ?";
        List<MPA> mpaList = jdbcTemplate.query(sqlMpa, FilmDBStorage::makeMpa, mpaId);
        film.setMpa(mpaList.get(0));
    }

    @Override
    public void update(Film film) throws ValidationException, DataNotFoundException {

        //Валидация film (update)
        String sqlFilmIdList = "SELECT FILM_ID FROM FILMS";
        List<Long> filmIdList = jdbcTemplate.query(
                sqlFilmIdList, (rs, rowNum) -> rs.getLong("FILM_ID"));
        FilmValidator.validateUpdate(filmIdList, film);

        //Валидация mpa
        MPA mpa = film.getMpa();
        Integer mpaId = mpa.getId();
        String sqlMpaIdList = "SELECT RATING_ID FROM RATINGS";
        List<Integer> mpaIdList = jdbcTemplate.query(
                sqlMpaIdList, (rs, rowNum) -> rs.getInt("RATING_ID"));
        FilmValidator.validateMPA(mpaIdList, mpaId);

        //Валидация genre
        Set<Integer> genreSet = film.getGenres()
                .stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());
        String sqlGenreIdList = "SELECT CATEGORY_ID FROM CATEGORIES";
        List<Integer> genreIdList = jdbcTemplate.query(
                sqlGenreIdList, (rs, rowNum) -> rs.getInt("CATEGORY_ID"));
        for (Integer id : genreSet) {
            FilmValidator.validateGenre(genreIdList, id);
        }

        //Запись film в DB
        String sqlUpdateUser = "UPDATE FILMS SET " +
                "FILM_NAME = ?, " +
                "DESCRIPTION = ?, " +
                "RELEASE_DATE = ?, " +
                "DURATION = ?, " +
                "RATING = ? " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlUpdateUser
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId());

        //Удаление записи film_genre в DB
        String sqlDeleteFilmGenre = "DELETE FROM FILM_CATEGORIES " +
                "WHERE FILM_ID = ? AND CATEGORY_ID = ?";
        for(Integer genreId : genreIdList) {
            jdbcTemplate.update(sqlDeleteFilmGenre, film.getId(), genreId);
        }

        //Запись film_genres в DB
        String sqlCreateFilmGenre = "INSERT INTO FILM_CATEGORIES(" +
                "FILM_ID, " +
                "CATEGORY_ID) " +
                "VALUES (?, ?)";
        for (Integer genreId : genreSet) {
            jdbcTemplate.update(sqlCreateFilmGenre
                    , film.getId()
                    , genreId
            );
        }

        //Доведение film к модели
        String sqlGenres = "SELECT * FROM CATEGORIES";
        List<Genre> allGenres = jdbcTemplate.query(sqlGenres, FilmDBStorage::makeGenre);
        List<Genre> filmGenres = allGenres
                .stream()
                .filter(genre -> genreSet.contains(genre.getId()))
                .collect(Collectors.toList());
        film.setGenres(filmGenres);
        String sqlMpa = "SELECT * FROM RATINGS WHERE RATING_ID = ?";
        List<MPA> mpaList = jdbcTemplate.query(sqlMpa, FilmDBStorage::makeMpa, mpaId);
        film.setMpa(mpaList.get(0));
    }

    @Override
    public List<Film> getFilmList() {
        //Создание film с пустым списком жанров
        String sqlFilmById = "SELECT * FROM FILMS LEFT OUTER JOIN RATINGS ON FILMS.RATING = RATINGS.RATING_ID";
        List<Film> films = jdbcTemplate.query(sqlFilmById, FilmDBStorage::makeFilm);

        //Создание списка жанров
        String sqlGenres = "SELECT * FROM CATEGORIES";
        List<Genre> allGenres = jdbcTemplate.query(sqlGenres, FilmDBStorage::makeGenre);

        //Установка жанров для film
        for(Film film : films) {
            //Уточнение списка id жанров для film
            String sqlGenreIdListByFilm = "SELECT CATEGORY_ID FROM FILM_CATEGORIES WHERE FILM_ID = ?";
            List<Integer> genreIdList = jdbcTemplate.query(
                    sqlGenreIdListByFilm, (rs, rowNum) -> rs.getInt("CATEGORY_ID"),film.getId());
            //Создание списка жанров для конкретного film
            List<Genre> filmGenres = allGenres
                    .stream()
                    .filter(genre -> genreIdList.contains(genre.getId()))
                    .collect(Collectors.toList());
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

        //Создание списка жанров
        String sqlGenres = "SELECT * FROM CATEGORIES";
        List<Genre> allGenres = jdbcTemplate.query(sqlGenres, FilmDBStorage::makeGenre);

        //Уточнение списка id жанров для film
        String sqlGenreIdListByFilm = "SELECT CATEGORY_ID FROM FILM_CATEGORIES WHERE FILM_ID = ?";
        List<Integer> genreIdList = jdbcTemplate.query(
                sqlGenreIdListByFilm, (rs, rowNum) -> rs.getInt("CATEGORY_ID"),film.getId());

        //Создание списка жанров для конкретного film
        List<Genre> filmGenres = allGenres
                .stream()
                .filter(genre -> genreIdList.contains(genre.getId()))
                .collect(Collectors.toList());

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
                rs.getInt("CATEGORY_ID"),
                rs.getString("CATEGORY_NAME")
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
