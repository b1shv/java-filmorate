package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {
    Map<Integer, User> getUsers();
    User getUserById(int id);
    User addUser(User user);
    User updateUser(User user);
    void deleteUser(int id);
}
