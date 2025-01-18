package com.practice.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.practice.filmorate.annotations.AfterDecember28th1895;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    int id;
    @NotBlank(message = "Название не может быть пустым")
    String name;
    @Size(max = 200, message = "Описание фильма превышает лимит символов")
    String description;
    @AfterDecember28th1895
    LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной")
    int duration;
    Mpa mpa;

    final Set<Genre> genres = new HashSet<>();
    final Set<Integer> likes = new HashSet<>();
    final Set<Director> directors = new HashSet<>();


    public void addLike(int userId) {
        likes.add(userId);
    }

    public void removeLike(int userId) {
        likes.remove(userId);
    }

    @JsonIgnore
    public int getNumberOfLikes() {
        return likes.size();
    }

    public void addGenres(Collection<Genre> newGenres) {
        genres.addAll(newGenres);
    }

    public void addLikes(Collection<Integer> newLikes) {
        likes.addAll(newLikes);
    }

    public void addDirectors(Collection<Director> newDirectors) {
        directors.addAll(newDirectors);
    }
}
