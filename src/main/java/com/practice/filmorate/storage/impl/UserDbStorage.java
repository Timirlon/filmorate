package com.practice.filmorate.storage.impl;

import com.practice.filmorate.model.User;
import com.practice.filmorate.storage.FullDbStorage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDbStorage implements FullDbStorage<User> {
    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.empty();
    }

    @Override
    public User create(User user) {
        return user;
    }

    @Override
    public User update(User user) {
        return user;
    }
}
