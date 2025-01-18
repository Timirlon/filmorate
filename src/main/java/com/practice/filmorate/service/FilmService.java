package com.practice.filmorate.service;

import com.practice.filmorate.exceptions.NotFoundException;
import com.practice.filmorate.exceptions.ValidationException;
import com.practice.filmorate.model.Film;
import com.practice.filmorate.model.Genre;
import com.practice.filmorate.model.Mpa;
import com.practice.filmorate.model.User;
import com.practice.filmorate.storage.BaseStorage;
import com.practice.filmorate.storage.FullStorage;
import com.practice.filmorate.storage.impl.FilmDbStorage;
import com.practice.filmorate.utils.FilmLikesComparator;
import com.practice.filmorate.utils.FilmReleaseComparator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmDbStorage filmStorage;
    private final FullStorage<User> userStorage;
    private final BaseStorage<Mpa> mpaStorage;
    private final BaseStorage<Genre> genreStorage;

    public void addLike(int userId, int filmId) {
        Film film = findById(filmId);

        checkIfUserExists(userId);
        film.addLike(userId);

        filmStorage.addLike(userId, filmId);
    }

    public void removeLike(int userId, int filmId) {
        checkIfUserExists(userId);

        Film film = findById(filmId);

        film.removeLike(userId);
    }

    public List<Film> findAllPopular(int count) {
        return filmStorage.findAllPopular(count);
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findById(int id) {
        return filmStorage.findById(id).orElseThrow(() -> new NotFoundException("Недействительный id фильма: " + id));
    }

    public Film create(Film film) {
        validate(film);

        return filmStorage.create(film);
    }

    public Film update(Film film) {
        validate(film);

        findById(film.getId());

        return filmStorage.update(film);
    }

    public void delete(int id) {
        validate(findById(id));

        filmStorage.delete(id);
    }

    public List<Film> findByDirectorId(int directorId, String sortBy) {
        List<Film> films = filmStorage.findByDirectorId(directorId);

        if (sortBy != null && sortBy.equals("likes")) {
            films = films.stream().sorted(new FilmLikesComparator()).toList();
        }

        if (sortBy != null && sortBy.equals("year")) {
            films = films.stream().sorted(new FilmReleaseComparator()).toList();
        }


        return films;
    }

    private void checkIfUserExists(int id) {
        userStorage.findById(id).orElseThrow(() -> new NotFoundException("Недействительный id пользователя: " + id));
    }

    private void validate(Film film) {
        int mpaId = film.getMpa().getId();
        mpaStorage.findById(mpaId).orElseThrow(() -> new ValidationException("Фильму задан некорректный MPA с id: " + mpaId));

        List<Integer> genres = genreStorage.findAll().stream()
                .map(Genre::getId)
                .toList();

        for (Genre genre : film.getGenres()) {
            if (!genres.contains(genre.getId())) {
                throw new ValidationException("Фильму присвоен некорректный жанр с id: " + genre.getId());
            }
        }
    }
}
