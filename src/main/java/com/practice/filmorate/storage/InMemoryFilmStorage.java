package com.practice.filmorate.storage;

import com.practice.filmorate.exceptions.NotFoundException;
import com.practice.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int uniqueId = 1;

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> findById(int id) {
        return Optional.of(films.get(id));
    }

    @Override
    public Film create(Film film) {
        film.setId(uniqueId++);

        films.put(film.getId(), film);
        log.info("Фильм {} добавлен.", film.getName());

        return film;
    }

    @Override
    public Film update(Film film) {
        int filmId = film.getId();

        if (!films.containsKey(filmId)) {
            log.warn("Не удалось обновить - фильм не найден.");
            throw new NotFoundException("Фильм не найден.");
        }

        films.put(filmId, film);
        log.info("Фильм {} обновлен.", film.getName());

        return film;
    }
}
