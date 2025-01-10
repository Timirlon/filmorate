package com.practice.filmorate.service;

import com.practice.filmorate.exceptions.NotFoundException;
import com.practice.filmorate.model.Film;
import com.practice.filmorate.model.User;
import com.practice.filmorate.storage.FullDbStorage;
import com.practice.filmorate.utils.FilmComparator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FullDbStorage<Film> filmStorage;
    private final FullDbStorage<User> userStorage;

    public void addLike(int userId, int filmId) {
        Film film = findById(filmId);

        checkIfUserExists(userId);
        film.addLike(userId);

    }

    public void removeLike(int userId, int filmId) {
        checkIfUserExists(userId);

        Film film = findById(filmId);

        film.removeLike(userId);
    }

    public List<Film> findAllPopular(int count) {
        return filmStorage.findAll()
                .stream()
                .sorted(new FilmComparator().reversed())
                .limit(count)
                .toList();
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findById(int id) {
        if (filmStorage.findById(id).isEmpty()) {
            throw new NotFoundException("Недействительный id фильма: " + id);
        }

        return filmStorage.findById(id).get();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {

        return filmStorage.update(film);
    }

    private void checkIfUserExists(int id) {
        userStorage.findById(id).orElseThrow(() -> new NotFoundException("Недействительный id пользователя: " + id));
    }
}
