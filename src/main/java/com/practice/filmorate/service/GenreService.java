package com.practice.filmorate.service;

import com.practice.filmorate.model.Genre;
import com.practice.filmorate.storage.BasicDbStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final BasicDbStorage<Genre> genreStorage;

    public List<Genre> findAll() {
        return genreStorage.findAll();
    }

    public Genre findById(int id) {
        return genreStorage.findById(id).orElseThrow();
    }
}
