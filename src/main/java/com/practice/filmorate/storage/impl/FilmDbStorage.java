/*
package com.practice.filmorate.storage.impl;

import com.practice.filmorate.model.Film;
import com.practice.filmorate.storage.FilmStorage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Component
public class FilmDbStorage implements FilmStorage {
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query("SELECT * FROM films;", this::mapRow);
    }

    @Override
    public Optional<Film> findById(int id) {
        jdbcTemplate.query("SELECT * FROM films WHERE id = ?;", this::mapRow, id)
                .stream()
                .findFirst()
                .orElseThrow();
        return Optional.empty();
    }

    @Override
    public Film create(Film film) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    private Film mapRow(ResultSet rs, int rowNum) {

    }
}
*/
