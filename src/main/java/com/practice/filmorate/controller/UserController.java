package com.practice.filmorate.controller;

import com.practice.filmorate.exceptions.UserNotFoundException;
import com.practice.filmorate.model.User;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int uniqueId = 1;

    @GetMapping
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        user.setId(uniqueId++);
        log.info("Пользователь {} создан.", user.getName());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        int userId = user.getId();

        if (!users.containsKey(userId)) {
            log.warn("Не удалось обновить - пользователь не найден.");
            throw new UserNotFoundException();
        }

        log.info("Пользователь {} обновлен.", user.getName());
        users.put(userId, user);
        return user;
    }
}
