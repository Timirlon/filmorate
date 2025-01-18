package com.practice.filmorate.service;

import com.practice.filmorate.exceptions.NotFoundException;
import com.practice.filmorate.model.Director;
import com.practice.filmorate.storage.FullStorage;
import com.practice.filmorate.storage.impl.DirectorDbStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorDbStorage storage;

    public List<Director> findAll() {
        return storage.findAll();
    }

    public Director findById(int id) {
        return storage.findById(id).orElseThrow(() -> new NotFoundException("Некорректный id режиссера: " + id));
    }

    public Director create(Director director) {
        return storage.create(director);
    }

    public Director update(Director director) {
        findById(director.getId());

        return storage.update(director);
    }

    public void delete(int id) {
        findById(id);

        storage.delete(id);
    }
}
