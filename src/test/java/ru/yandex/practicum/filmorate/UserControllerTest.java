package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

class UserControllerTest {
    @Test
    void updateShouldThrowNotFoundException_ifWrongId() {
        UserController userController = new UserController();

        User user1 = new User();
        User user2 = new User();
        User user3 = new User();

        userController.create(user1);
        userController.create(user2);
        userController.create(user3);

        User user1Updated = new User();
        user1Updated.setId(1);
        user1Updated.setName("User 1");

        User user3Updated = new User();
        user3Updated.setId(3);
        user3Updated.setName("User 3");

        User userWithWrongId = new User();
        userWithWrongId.setId(44);

        userController.update(user1Updated);
        userController.update(user3Updated);

        List<User> expected = List.of(user1Updated, user2, user3Updated);
        List<User> actual = userController.findAll();

        Assertions.assertEquals(expected, actual);
        Assertions.assertThrows(NotFoundException.class, () -> userController.update(userWithWrongId));
    }

}
