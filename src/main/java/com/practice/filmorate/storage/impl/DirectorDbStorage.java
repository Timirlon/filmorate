package com.practice.filmorate.storage.impl;

import com.practice.filmorate.model.Director;
import com.practice.filmorate.storage.FullStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DirectorDbStorage implements FullStorage<Director> {
    private final JdbcTemplate jdbcTemplate;
    private static final String GET_ALL_QUERY = """
            SELECT * FROM directors
            """;

    @Override
    public List<Director> findAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, this::rowMap);
    }

    @Override
    public Optional<Director> findById(int id) {
        String getById = GET_ALL_QUERY + " WHERE id = ?";

        return jdbcTemplate.query(getById, this::rowMap, id)
                .stream()
                .findFirst();
    }

    @Override
    public Director create(Director director) {
        SimpleJdbcInsert directorsInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("directors")
                .usingGeneratedKeyColumns("id");

        int id = directorsInsert.executeAndReturnKey(Map.of("name", director.getName())).intValue();

        director.setId(id);


        return director;
    }

    @Override
    public Director update(Director director) {
        String update = """
                UPDATE directors
                SET name = ?
                WHERE id = ?
                """;

        jdbcTemplate.update(update, director.getName(), director.getId());

        return director;
    }

    public void deleteById(int id) {
        String delete = """
                DELETE FROM directors
                WHERE id = ?
                """;

        jdbcTemplate.update(delete, id);
    }

    private Director rowMap(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");

        return new Director(id, name);
    }
}
