package com.practice.filmorate.service;

import com.practice.filmorate.exceptions.NotFoundException;
import com.practice.filmorate.exceptions.ValidationException;
import com.practice.filmorate.model.User;
import com.practice.filmorate.storage.FullStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final FullStorage<User> storage;

    public List<User> findAll() {
        return storage.findAll();
    }

    public User findById(int id) {
        return storage.findById(id).orElseThrow(() -> new NotFoundException("Недействительный id пользователя: " + id));
    }

    public User create(User user) {
        validate(user);
        replaceUserNameIfItsBlank(user);

        return storage.create(user);
    }

    public User update(User user) {
        validate(user);
        replaceUserNameIfItsBlank(user);

        findById(user.getId());

        return storage.update(user);
    }

    public void addFriend(int userId, int friendId) {
        User user = findById(userId);
        User friend = findById(friendId);

        if (!user.isFriend(friendId)) {
            user.addFriend(friendId);

            storage.update(user);
        }
    }

    public void removeFriend(int userId, int friendId) {
        User user = findById(userId);
        User friend = findById(friendId);

        if (user.isFriend(friendId) || friend.isFriend(userId)) {
            user.removeFriend(friendId);
            storage.update(user);
        }
    }

    public List<User> findAllFriends(int userId) {
        User user = findById(userId);

        return user.getFriends().stream()
                .map(storage::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public List<User> findCommonFriends(int userId, int friendId) {
        User user = findById(userId);
        User friend = findById(friendId);

        return user.getFriends()
                .stream()
                .filter(commonFriend -> friend.getFriends().contains(commonFriend))
                .map(this::findById)
                .toList();
    }

    private void replaceUserNameIfItsBlank(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void validate(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы.");
        }
    }
}
