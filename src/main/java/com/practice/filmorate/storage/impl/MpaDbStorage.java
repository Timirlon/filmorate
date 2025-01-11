package com.practice.filmorate.storage.impl;

import com.practice.filmorate.model.Mpa;
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
public class MpaDbStorage implements BaseStorage<Mpa> {
    private final JdbcTemplate jdbcTemplate;
    private static final String GET_ALL_QUERY = "SELECT id, name, description FROM mpa";

    @Override
    public List<Mpa> findAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, this::mapRow);
    }

    @Override
    public Optional<Mpa> findById(int id) {
        String getByIdQuery = GET_ALL_QUERY + " WHERE id = ?";

        return jdbcTemplate.query(getByIdQuery, this::mapRow, id)
                .stream()
                .findFirst();
    }

    private Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String desc = rs.getString("description");

        return new Mpa(id, name, desc);
    }
}
