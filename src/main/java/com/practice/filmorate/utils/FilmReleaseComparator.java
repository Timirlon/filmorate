package com.practice.filmorate.utils;

import com.practice.filmorate.model.Film;

import java.util.Comparator;

public class FilmReleaseComparator implements Comparator<Film> {
    public int compare(Film f1, Film f2) {
        if (f2.getReleaseDate() == null) {
            return 1;
        }

        if (f1.getReleaseDate() == null) {
            return -1;
        }

        if (f1.getReleaseDate().isAfter(f2.getReleaseDate())) {
            return 1;
        } else if (f1.getReleaseDate().isBefore(f2.getReleaseDate())) {
            return -1;
        } else {
            return 0;
        }
    }
}
