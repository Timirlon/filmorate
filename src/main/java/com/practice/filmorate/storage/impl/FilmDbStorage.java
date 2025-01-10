package com.practice.filmorate.storage.impl;

import com.practice.filmorate.model.Film;
import com.practice.filmorate.model.Genre;
import com.practice.filmorate.model.Mpa;
import com.practice.filmorate.storage.FullDbStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FullDbStorage<Film> {
    private final JdbcTemplate jdbcTemplate;

    private static final String GET_ALL_QUERY = """
            SELECT f.id AS film_id, f.name AS film_name, f.description AS film_desc, f.RELEASE_DATE AS release, f.duration AS duration,
            g.id AS genre_id, g.name AS genre_name, m.id AS mpa_id, m.name AS mpa_name, m.description AS mpa_desc
            FROM films f
            JOIN films_genres fg ON f.id = fg.film_id
            JOIN genres g ON fg.genre_id = g.id
            JOIN mpa m ON f.mpa_id = m.id
            """;

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, this::mapRow);
    }

    @Override
    public Optional<Film> findById(int id) {
        String query = GET_ALL_QUERY + " WHERE f.id = ?";

        return Optional.of(jdbcTemplate.query(query, this::mapRow, id)
                .stream()
                .findFirst()
                .orElseThrow());
    }

    @Override
    public Film create(Film film) {
        int id = insertIntoFilmsTableAndReturnKey(film).intValue();
        film.setId(id);

        insertIntoFilmsGenresTable(film);

        return film;
    }

    @Override
    public Film update(Film film) {
        updateFilmsTable(film);

        updateFilmsGenresTable(film);

        return film;
    }

    private Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        int filmId = rs.getInt("film_id");
        String filmName = rs.getString("film_name");
        String filmDesc = rs.getString("film_desc");
        LocalDate releaseDate = rs.getDate("release").toLocalDate();
        int duration = rs.getInt("duration");

        int genreId = rs.getInt("genre_id");
        String genreName = rs.getString("genre_name");
        Genre genre = new Genre(genreId, genreName);

        int mpaId = rs.getInt("mpa_id");
        String mpaName = rs.getString("mpa_name");
        String mpaDesc = rs.getString("mpa_desc");
        Mpa mpa = new Mpa(mpaId, mpaName, mpaDesc);


        return new Film(filmId, filmName, filmDesc, releaseDate, duration, genre, mpa);
    }

    private Number insertIntoFilmsTableAndReturnKey(Film film) {
        SimpleJdbcInsert FilmsInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> map = Map.of(
                "name", film.getName(),
                "description", film.getDescription(),
                "release_date", film.getReleaseDate(),
                "duration", film.getDuration(),
                "mpa_id", film.getMpa().getId()
        );

        return FilmsInsert.executeAndReturnKey(map);
    }

    private void insertIntoFilmsGenresTable(Film film) {
        SimpleJdbcInsert filmsGenresInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films_genres");

        Map<String, Object> filmsGenresMap = Map.of(
                "film_id", film.getId(),
                "genre_id", film.getGenre().getId()
        );

        filmsGenresInsert.execute(filmsGenresMap);
    }

    private void updateFilmsTable(Film film) {
        String updateFilmsQuery = """                
                UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ?
                WHERE id = ?
                """;

        jdbcTemplate.update(
                updateFilmsQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId()
        );
    }

    private void updateFilmsGenresTable(Film film) {
        String updateFilmsGenresQuery = "UPDATE films_genres SET genre_id = ? WHERE film_id = ?";

        jdbcTemplate.update(updateFilmsGenresQuery, film.getGenre().getId(), film.getId());
    }
}
