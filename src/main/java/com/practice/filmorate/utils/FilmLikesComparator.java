package com.practice.filmorate.utils;

import com.practice.filmorate.model.Film;

import java.util.Comparator;

public class FilmLikesComparator implements Comparator<Film> {
    public int compare(Film f1, Film f2) {
        return Integer.compare(f1.getNumberOfLikes(), f2.getNumberOfLikes());
    }
}
