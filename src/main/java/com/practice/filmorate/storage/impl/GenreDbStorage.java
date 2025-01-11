package com.practice.filmorate.storage.impl;

import com.practice.filmorate.model.Genre;
import com.practice.filmorate.storage.BaseStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements BaseStorage<Genre> {
    private final JdbcTemplate jdbcTemplate;

    private static final String GET_ALL_QUERY = "SELECT * FROM genres";

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, this::mapRow);
    }

    @Override
    public Optional<Genre> findById(int id) {
        String getByIdQuery = GET_ALL_QUERY + " WHERE id = ?";

        return jdbcTemplate.query(getByIdQuery, this::mapRow, id)
                .stream()
                .findFirst();
    }

    private Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");

        return new Genre(id, name);
    }
}
