package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage storage;
    private int idCounter = 0;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public List<User> getUsers() {
        return new ArrayList<>(storage.getUsers().values());
    }

    public User getUserById(int id) {
        checkUserId(id);
        return storage.getUserById(id);
    }

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(++idCounter);
        log.debug("POST request handled: new user added");
        return storage.addUser(user);
    }

    public User updateUser(User user) {
        checkUserId(user.getId());

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        log.debug(String.format("PUT request handled: user %d is updated", user.getId()));
        return storage.updateUser(user);
    }

    public void deleteUser(int id) {
        checkUserId(id);
        storage.deleteUser(id);
        log.debug(String.format("DELETE request handled: user %d is deleted", id));
    }

    public void addFriend(int id1, int id2) {
        checkUserId(id1);
        checkUserId(id2);

        storage.getUserById(id1).addFriend(id2);
        storage.getUserById(id2).addFriend(id1);
        log.debug(String.format("POST request handled: users %d and %d are now friends", id1, id2));
    }

    public void deleteFriend(int id1, int id2) {
        checkUserId(id1);
        checkUserId(id2);

        if (!storage.getUserById(id1).getFriends().contains(id2)
            || !storage.getUserById(id2).getFriends().contains(id1)) {
            throw new NotFoundException(String.format("Users %d and %d are not friends", id1, id2));
        }

        storage.getUserById(id1).deleteFriend(id2);
        storage.getUserById(id2).deleteFriend(id1);
    }

    public List<User> getFriends(int id) {
        return storage.getUserById(id).getFriends().stream()
                .map(storage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int id1, int id2) {
        checkUserId(id1);
        checkUserId(id2);

        return storage.getUserById(id1).getFriends().stream()
                .filter(id -> storage.getUserById(id2).getFriends().contains(id))
                .map(storage :: getUserById)
                .collect(Collectors.toList());
    }

    protected void checkUserId(int id) {
        if (!storage.getUsers().containsKey(id)) {
            throw new NotFoundException(String.format("User %d is not found", id));
        }
    }
}
