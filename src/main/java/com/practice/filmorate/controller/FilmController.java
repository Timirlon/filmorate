//package com.practice.filmorate.controller;
//
//import com.practice.filmorate.model.Film;
//import com.practice.filmorate.service.FilmService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@Slf4j
//@RequiredArgsConstructor
//@RequestMapping("/films")
//public class FilmController {
//    //
//    private final FilmService service;
//
//    @GetMapping
//    public List<Film> findAll() {
//        return service.filmStorage.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public Film findById(@PathVariable int id) {
//        return service.findById(id);
//    }
//
//    @PostMapping
//    public Film create(@Valid @RequestBody Film film) {
//        return service.filmStorage.create(film);
//    }
//
//    @PutMapping
//    public Film update(@Valid @RequestBody Film film) {
//        return service.filmStorage.update(film);
//    }
//
//    @PutMapping("/{id}/like/{userId}")
//    public void addLike(@PathVariable("id") int filmId, @PathVariable int userId) {
//        service.addLike(userId, filmId);
//    }
//
//    @DeleteMapping("/{id}/like/{userId}")
//    public void removeLike(@PathVariable("id") int filmId, @PathVariable int userId) {
//        service.removeLike(userId, filmId);
//    }
//
//    @GetMapping("/popular")
//    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") int count) {
//        return service.findAllPopular(count);
//    }
//}
