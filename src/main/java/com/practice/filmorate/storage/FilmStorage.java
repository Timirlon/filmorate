package com.practice.filmorate.storage;

import com.practice.filmorate.model.Film;

import java.util.*;

public interface FilmStorage {
    Map<Integer, Film> films = new HashMap<>();

    List<Film> findAll();

    Optional<Film> findById(int id);

    Film create(Film film);

    Film update(Film film);
}
