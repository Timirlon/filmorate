package com.practice.filmorate.controller;

import com.practice.filmorate.model.Director;
import com.practice.filmorate.service.DirectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService service;

    @GetMapping
    public List<Director> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Director findById(@PathVariable int id) {
        return service.findById(id);
    }

    @PostMapping
    public Director create(@RequestBody Director director) {
        return service.create(director);
    }

    @PutMapping
    public Director update(@RequestBody Director director) {
        return service.update(director);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable int id) {
        service.deleteById(id);
    }
}
