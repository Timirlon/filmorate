package com.practice.filmorate.service;

import com.practice.filmorate.exceptions.NotFoundException;
import com.practice.filmorate.model.Mpa;
import com.practice.filmorate.storage.BaseStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final BaseStorage<Mpa> mpaStorage;

    public List<Mpa> findAll() {
        return mpaStorage.findAll();
    }

    public Mpa findById(int id) {
        return mpaStorage.findById(id).orElseThrow(() -> new NotFoundException("Некорректный id mpa: " + id));
    }
}
