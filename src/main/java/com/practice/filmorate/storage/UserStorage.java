package com.practice.filmorate.storage;

import com.practice.filmorate.model.User;

import java.util.*;

public interface UserStorage {
    Map<Integer, User> users = new HashMap<>();

    List<User> findAll();

    Optional<User> findById(int id);

    User create(User user);

    User update(User user);
}
