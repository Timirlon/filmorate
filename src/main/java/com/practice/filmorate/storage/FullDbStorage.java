package com.practice.filmorate.storage;

import com.practice.filmorate.model.Film;

public interface FullDbStorage<T> extends BasicDbStorage<T> {
    T create(T t);

    T update(T t);
}
