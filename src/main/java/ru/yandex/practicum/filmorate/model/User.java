package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private int id;
    private String name;
    private final Set<Integer> friendIds = new HashSet<>();

    @NotBlank(message = "field email should not be empty")
    @Email(message = "wrong email format")
    private String email;

    @NotBlank(message = "field login should not be empty")
    @Pattern(regexp = "^\\S+$", message = "login should consist of letters")
    private String login;

    @Past(message = "birthday can't be in the future")
    @NotNull(message = "field birthday should not be empty")
    private LocalDate birthday;

    public void addFriend(int id) {
        friendIds.add(id);
    }

    public void deleteFriend(int id) {
        friendIds.remove(id);
    }
}
