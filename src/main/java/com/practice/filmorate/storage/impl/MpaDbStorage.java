package com.practice.filmorate.storage.impl;

import com.practice.filmorate.model.Mpa;
import com.practice.filmorate.storage.BasicDbStorage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MpaDbStorage implements BasicDbStorage<Mpa> {
    @Override
    public List<Mpa> findAll() {
        return null;
    }

    @Override
    public Optional<Mpa> findById(int id) {
        return Optional.empty();
    }
}
