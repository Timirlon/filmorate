package com.practice.filmorate.controller;

import com.practice.filmorate.exceptions.FilmNotFoundException;
import com.practice.filmorate.model.Film;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Фильм {} добавлен.", film.getName());
        return films.put(film.getId(), film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        int filmId = film.getId();

        if (!films.containsKey(filmId)) {
            log.warn("Не удалось обновить - фильм не найден.");
            throw new FilmNotFoundException();
        }

        log.info("Фильм {} обновлен.", film.getName());
        return films.put(filmId, film);
    }
}
