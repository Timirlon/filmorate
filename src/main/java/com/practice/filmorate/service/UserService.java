package com.practice.filmorate.service;

import com.practice.filmorate.exceptions.NotFoundException;
import com.practice.filmorate.model.User;
import com.practice.filmorate.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    public final UserStorage storage;

    public void addFriend(int userId, int friendId) {
        User user = findById(userId);
        User friend = findById(friendId);

        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    public void removeFriend(int userId, int friendId) {
        User user = findById(userId);
        User friend = findById(friendId);

        user.removeFriend(friendId);
        friend.removeFriend(userId);
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
                .map(storage::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public User findById(int id) {
        if (storage.findById(id).isEmpty()) {
            throw new NotFoundException("Недействительный id пользователя: " + id);
        }

        return storage.findById(id).get();
    }
}
