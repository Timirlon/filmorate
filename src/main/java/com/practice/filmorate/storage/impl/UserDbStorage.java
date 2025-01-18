package com.practice.filmorate.storage.impl;

import com.practice.filmorate.model.User;
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
public class UserDbStorage implements FullStorage<User> {
    private final JdbcTemplate jdbcTemplate;

    private final static String GET_ALL_QUERY = "SELECT id, email, login, name, birthday FROM users";

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, this::mapRow);
    }

    @Override
    public Optional<User> findById(int id) {
        String getByIdQuery = GET_ALL_QUERY + " WHERE id = ?";

        Optional<User> user = jdbcTemplate.query(getByIdQuery, this::mapRow, id)
                .stream()
                .findFirst();

        user.ifPresent(value -> value.setFriends(getFriendsByUserId(id)));


        return user;
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert filmsInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> usersMap = Map.of(
                "email", user.getEmail(),
                "login", user.getLogin(),
                "name", user.getName(),
                "birthday", user.getBirthday()
        );

        int id = filmsInsert.executeAndReturnKey(usersMap).intValue();
        user.setId(id);

        insertIntoFriendsTable(user);

        return user;
    }

    @Override
    public User update(User user) {
        String updateQuery = """
                UPDATE users SET email = ?, login = ?, name = ?, birthday = ?
                WHERE id = ?
                """;

        jdbcTemplate.update(
                updateQuery, user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), user.getId()
        );

        updateFriendsTable(user);

        return user;
    }

    @Override
    public void delete(int id) {
        String deleteUsersQuery = """
                DELETE FROM users
                WHERE id = ?
                """;

        String deleteFriendsQuery = """
                DELETE FROM friends
                WHERE user_id = ?
                OR friend_id = ?
                """;

        String deleteLikesQuery = """
                DELETE FROM likes
                WHERE user_id = ?
                """;

        jdbcTemplate.update(deleteFriendsQuery, id, id);
        jdbcTemplate.update(deleteLikesQuery, id);
        jdbcTemplate.update(deleteUsersQuery, id);
    }

    private User mapRow(ResultSet rs, int rowNum) throws SQLException {
        int userId = rs.getInt("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();

        return new User(userId, email, login, name, birthday);
    }

    private int mapRowFriends(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("friend_id");
    }

    private Set<Integer> getFriendsByUserId(int id) {
        String getFriendsQuery = """
                SELECT friend_id FROM friends
                WHERE user_id = ?
                """;

        return new HashSet<>(jdbcTemplate.query(getFriendsQuery, this::mapRowFriends, id));
    }

    private void insertIntoFriendsTable(User user) {
        if (user.getFriends().isEmpty()) {
            return;
        }

        SimpleJdbcInsert filmsGenresInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("friends");

        for (int friendId : user.getFriends()) {
            Map<String, Object> friendsMap = Map.of(
                    "user_id", user.getId(),
                    "friend_id", friendId
            );

            filmsGenresInsert.execute(friendsMap);
        }
    }

    private void updateFriendsTable(User user) {
        String deleteQuery = "DELETE FROM friends WHERE user_id = ?";
        jdbcTemplate.update(deleteQuery, user.getId());

        insertIntoFriendsTable(user);
    }
}
