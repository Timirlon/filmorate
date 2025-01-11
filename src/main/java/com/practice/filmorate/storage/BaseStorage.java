package com.practice.filmorate.storage;

import java.util.List;
import java.util.Optional;

public interface BaseStorage<T> {
    List<T> findAll();

    Optional<T> findById(int id);
}
