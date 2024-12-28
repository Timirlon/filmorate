package com.practice.filmorate.service;

import com.practice.filmorate.exceptions.NotFoundException;
import com.practice.filmorate.model.Film;
import com.practice.filmorate.storage.FilmStorage;
import com.practice.filmorate.storage.UserStorage;
import com.practice.filmorate.utils.FilmComparator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    public final FilmStorage filmStorage;
    public final UserStorage userStorage;

    public void addLike(int userId, int filmId) {
        Film film = findById(filmId);

        if (checkIfUserExists(userId)) {
            film.addLike(userId);
        }
    }

    public void removeLike(int userId, int filmId) {
        Film film = findById(filmId);

        film.removeLike(userId);
    }

    public List<Film> findAllPopular(int count) {
        return filmStorage.findAll()
                .stream()
                .sorted(new FilmComparator())
                .limit(count)
                .toList();
    }

    public Film findById(int id) {
        if (filmStorage.findById(id).isEmpty()) {
            throw new NotFoundException("Недействительный id фильма: " + id);
        }

        return filmStorage.findById(id).get();
    }

    private boolean checkIfUserExists(int id) {
        return userStorage.findById(id).isPresent();
    }
}
