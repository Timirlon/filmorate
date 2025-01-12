package com.practice.filmorate.service;

import com.practice.filmorate.exceptions.NotFoundException;
import com.practice.filmorate.exceptions.ValidationException;
import com.practice.filmorate.model.Film;
import com.practice.filmorate.model.Genre;
import com.practice.filmorate.model.Mpa;
import com.practice.filmorate.model.User;
import com.practice.filmorate.storage.BaseStorage;
import com.practice.filmorate.storage.FullStorage;
import com.practice.filmorate.utils.FilmComparator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FullStorage<Film> filmStorage;
    private final FullStorage<User> userStorage;
    private final BaseStorage<Mpa> mpaStorage;
    private final BaseStorage<Genre> genreStorage;

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
        return filmStorage.findById(id).orElseThrow(() -> new NotFoundException("Недействительный id фильма: " + id));
    }

    public Film create(Film film) {
        validateFilm(film);

        return filmStorage.create(film);
    }

    public Film update(Film film) {
        validateFilm(film);

        findById(film.getId());

        return filmStorage.update(film);
    }

    private void checkIfUserExists(int id) {
        userStorage.findById(id).orElseThrow(() -> new NotFoundException("Недействительный id пользователя: " + id));
    }

    private void validateFilm(Film film) {
        int mpaId = film.getMpa().getId();
        mpaStorage.findById(mpaId).orElseThrow(() -> new ValidationException("Фильму задан некорректный MPA с id: " + mpaId));

        List<Integer> genres = genreStorage.findAll().stream()
                .map(Genre::getId)
                .toList();

        for (int genreId : film.getGenres().stream().map(Genre::getId).toList()) {
            if (!genres.contains(genreId)) {
                throw new ValidationException("Фильму присвоен некорректный жанр с id: " + genreId);
            }
        }
    }
}
