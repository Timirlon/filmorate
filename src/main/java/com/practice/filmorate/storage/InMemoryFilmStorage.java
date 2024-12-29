package com.practice.filmorate.storage;

import com.practice.filmorate.exceptions.NotFoundException;
import com.practice.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int uniqueId = 1;

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> findById(int id) {
        return Optional.ofNullable(films.get(id));
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

        // вынести в сервис
        if (!films.containsKey(filmId)) {
            log.warn("Не удалось обновить - фильм не найден.");
            throw new NotFoundException("Фильм не найден.");
        }

        films.put(filmId, film);
        log.info("Фильм {} обновлен.", film.getName());

        return film;
    }
}
