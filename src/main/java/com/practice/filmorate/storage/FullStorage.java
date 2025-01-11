package com.practice.filmorate.storage;

public interface FullStorage<T> extends BaseStorage<T> {
    T create(T t);

    T update(T t);
}
