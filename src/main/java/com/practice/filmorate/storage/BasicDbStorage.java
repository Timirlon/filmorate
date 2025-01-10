package com.practice.filmorate.storage;

import java.util.List;
import java.util.Optional;

public interface BasicDbStorage<T> {
    List<T> findAll();

    Optional<T> findById(int id);
}
