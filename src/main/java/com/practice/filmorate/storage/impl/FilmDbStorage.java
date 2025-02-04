package com.practice.filmorate.storage.impl;

import com.practice.filmorate.model.Director;
import com.practice.filmorate.model.Film;
import com.practice.filmorate.model.Genre;
import com.practice.filmorate.model.Mpa;
import com.practice.filmorate.storage.FullStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FullStorage<Film> {
    private final JdbcTemplate jdbcTemplate;

    private static final String GET_ALL_QUERY = """
            SELECT f.id AS film_id,
                f.name AS film_name,
                f.description AS film_desc,
                f.release_date AS release,
                f.duration AS duration,
                m.id AS mpa_id,
                m.name AS mpa_name,
                m.description AS mpa_desc
            FROM films f
            JOIN mpa m ON f.mpa_id = m.id
            """;

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, this::mapRow);
    }

    @Override
    public Optional<Film> findById(int id) {
        String getByIdQuery = GET_ALL_QUERY + " WHERE f.id = ?";

        Optional<Film> film = jdbcTemplate.query(getByIdQuery, this::mapRow, id)
                .stream()
                .findFirst();


        if (film.isPresent()) {
            Film value = film.get();

            value.addGenres(getGenresByFilmId(id));

            value.addLikes(getLikesByFilmId(id));

            value.addDirectors(getDirectorsByFilmId(id));
        }

        return film;
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert filmsInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> filmsMap = Map.of(
                "name", film.getName(),
                "description", film.getDescription(),
                "release_date", film.getReleaseDate(),
                "duration", film.getDuration(),
                "mpa_id", film.getMpa().getId()
        );

        int id = filmsInsert.executeAndReturnKey(filmsMap).intValue();
        film.setId(id);


        insertIntoFilmsGenresTable(film);

        insertIntoLikesTable(film);

        insertIntoFilmsDirectorsTable(film);


        return film;
    }

    @Override
    public Film update(Film film) {
        String updateFilmsQuery = """                
                UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ?
                WHERE id = ?
                """;

        jdbcTemplate.update(
                updateFilmsQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId()
        );

        updateFilmsGenresTable(film);

        updateLikesTable(film);

        updateFilmsDirectorsTable(film);


        return film;
    }

    @Override
    public void delete(int id) {
        String deleteFilmsQuery = """
                DELETE FROM films
                WHERE id = ?
                """;

        String deleteFilmsDirectorsQuery = """
                DELETE FROM films_directors
                WHERE film_id = ?
                """;

        String deleteFilmsGenresQuery = """
                DELETE FROM films_genres
                WHERE film_id = ?
                """;

        String deleteLikesQuery = """
                DELETE FROM likes
                WHERE film_id = ?
                """;


        jdbcTemplate.update(deleteFilmsDirectorsQuery, id);
        jdbcTemplate.update(deleteFilmsGenresQuery, id);
        jdbcTemplate.update(deleteLikesQuery, id);
        jdbcTemplate.update(deleteFilmsQuery, id);
    }

    public void addLike(int userId, int filmId) {
        String insertQuery = """
                INSERT INTO likes (film_id, user_id)
                VALUES (?, ?)
                """;

        jdbcTemplate.update(insertQuery, filmId, userId);
    }

    public List<Film> findAllPopular(int count) {
        String getPopularQuery = GET_ALL_QUERY + """               
                LEFT JOIN likes l ON f.id = l.film_id
                GROUP BY f.id
                ORDER BY COUNT(l.user_id) DESC
                LIMIT ?
                """;

        return jdbcTemplate.query(getPopularQuery, this::mapRow, count);
    }

    public List<Film> findByDirectorId(int directorId, String sortBy) {
        String getByDir = GET_ALL_QUERY + """
                JOIN films_directors fd
                              ON f.id = fd.film_id
                         JOIN directors d
                              ON fd.director_id = d.id
                         LEFT JOIN likes l
                              ON f.id = l.film_id
                WHERE d.id = ?
                group by f.id
                """;

        if (sortBy != null && sortBy.equals("likes")) {
            getByDir += "ORDER BY COUNT(l.user_id) DESC";
        }

        if (sortBy != null && sortBy.equals("year")) {
            getByDir += "ORDER BY f.release_date";
        }

        return jdbcTemplate.query(getByDir, this::mapRow, directorId)
                .stream()
                .peek(film -> film.addDirectors(getDirectorsByFilmId(film.getId())))
                .toList();
    }

    private Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        int filmId = rs.getInt("film_id");
        String filmName = rs.getString("film_name");
        String filmDesc = rs.getString("film_desc");
        LocalDate releaseDate = rs.getDate("release").toLocalDate();
        int duration = rs.getInt("duration");


        int mpaId = rs.getInt("mpa_id");
        String mpaName = rs.getString("mpa_name");
        String mpaDesc = rs.getString("mpa_desc");
        Mpa mpa = new Mpa(mpaId, mpaName, mpaDesc);


        return new Film(filmId, filmName, filmDesc, releaseDate, duration, mpa);
    }

    private Genre mapRowGenre(ResultSet rs, int rowNum) throws SQLException {
        int genreId = rs.getInt("id");
        String genreName = rs.getString("name");

        return new Genre(genreId, genreName);
    }

    private Integer mapRowLike(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("user_id");
    }

    private Director mapRowDirector(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");

        return new Director(id, name);
    }

    private Set<Genre> getGenresByFilmId(int id) {
        String getGenresQuery = """
                SELECT g.id, g.name FROM genres g
                JOIN films_genres fg
                ON g.id = fg.genre_id
                WHERE fg.film_id = ?
                """;

        return new HashSet<>(jdbcTemplate.query(getGenresQuery, this::mapRowGenre, id));
    }

    private Set<Integer> getLikesByFilmId(int id) {
        String getLikesQuery = """
                SELECT user_id FROM likes
                WHERE film_id = ?
                """;

        return new HashSet<>(jdbcTemplate.query(getLikesQuery, this::mapRowLike, id));
    }

    private Set<Director> getDirectorsByFilmId(int id) {
        String getDirectorsQuery = """
                SELECT d.id AS id, d.name AS name
                FROM directors d
                JOIN films_directors fd
                ON d.id = fd.director_id
                WHERE film_id = ?
                """;

        return new HashSet<>(jdbcTemplate.query(getDirectorsQuery, this::mapRowDirector, id));
    }

    private void insertIntoFilmsGenresTable(Film film) {
        if (film.getGenres().isEmpty()) {
            return;
        }

        String insertQuery = """
        INSERT INTO films_genres (film_id, genre_id)
        VALUES (?, ?)
        """;

        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(insertQuery, film.getId(), genre.getId());
        }
    }

    private void insertIntoLikesTable(Film film) {
        if (film.getLikes().isEmpty()) {
            return;
        }


        for (int userId : film.getLikes()) {
            addLike(userId, film.getId());
        }
    }

    private void insertIntoFilmsDirectorsTable(Film film) {
        if (film.getDirectors().isEmpty()) {
            return;
        }

        String insertQuery = """
        INSERT INTO films_directors (film_id, director_id)
        VALUES (?, ?)
        """;

        for (Director director : film.getDirectors()) {
            jdbcTemplate.update(insertQuery, film.getId(), director.getId());
        }
    }

    private void updateFilmsGenresTable(Film film) {
        String deleteQuery = "DELETE FROM films_genres WHERE film_id = ?";
        jdbcTemplate.update(deleteQuery, film.getId());

        insertIntoFilmsGenresTable(film);
    }

    private void updateLikesTable(Film film) {
        String deleteQuery = "DELETE FROM likes WHERE film_id = ?";
        jdbcTemplate.update(deleteQuery, film.getId());

        insertIntoLikesTable(film);
    }

    private void updateFilmsDirectorsTable(Film film) {
        String deleteQuery = "DELETE FROM films_directors WHERE film_id = ?";
        jdbcTemplate.update(deleteQuery, film.getId());

        insertIntoFilmsDirectorsTable(film);
    }
}
