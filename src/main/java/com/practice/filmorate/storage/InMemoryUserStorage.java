package com.practice.filmorate.storage;

import com.practice.filmorate.exceptions.NotFoundException;
import com.practice.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private int uniqueId = 1;

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.of(users.get(id));
    }

    @Override
    public User create(User user) {
        user.setId(uniqueId++);

        users.put(user.getId(), user);
        log.info("Пользователь {} создан.", user.getName());

        return user;
    }

    @Override
    public User update(User user) {
        int userId = user.getId();

        if (!users.containsKey(userId)) {
            log.warn("Не удалось обновить - пользователь не найден.");
            throw new NotFoundException("Пользователь не найден.");
        }

        users.put(userId, user);
        log.info("Пользователь {} обновлен.", user.getName());

        return user;
    }
}
