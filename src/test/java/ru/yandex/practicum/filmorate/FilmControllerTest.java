package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    public void filmControllerInit() {
        filmController = new FilmController();
    }

    @Test
    void createShouldThrowValidationException_ifIncorrectReleaseDate() {
        Film film1 = new Film();
        film1.setReleaseDate(LocalDate.of(1700, 11, 15));

        Film film2 = new Film();
        film2.setReleaseDate(LocalDate.of(1895, 12, 27));

        Film film3 = new Film();
        film3.setReleaseDate(LocalDate.of(1895, 12, 28));

        Film film4 = new Film();
        film4.setReleaseDate(LocalDate.of(1999, 1, 2));

        Assertions.assertThrows(ValidationException.class, () -> filmController.create(film1));
        Assertions.assertThrows(ValidationException.class, () -> filmController.create(film2));
        Assertions.assertDoesNotThrow(() -> filmController.create(film3));
        Assertions.assertDoesNotThrow(() -> filmController.create(film4));

    }

    @Test
    void updateShouldThrowNotFoundException_ifWrongId() {
        Film film1 = new Film();
        film1.setReleaseDate(LocalDate.of(2000,5,12));
        Film film2 = new Film();
        film2.setReleaseDate(LocalDate.of(2001,6,13));
        Film film3 = new Film();
        film3.setReleaseDate(LocalDate.of(2002,7,14));

        filmController.create(film1);
        filmController.create(film2);
        filmController.create(film3);

        Film film1Updated = new Film();
        film1Updated.setName("Film 1");
        film1Updated.setReleaseDate(LocalDate.of(2000,5,12));
        film1Updated.setId(1);

        Film film3Updated = new Film();
        film3Updated.setReleaseDate(LocalDate.of(2005,12,15));
        film3Updated.setName("Film 3");
        film3Updated.setId(3);

        Film filmWithWrongId = new Film();
        filmWithWrongId.setId(33);
        filmWithWrongId.setReleaseDate(LocalDate.of(1999,1,1));

        filmController.update(film1Updated);
        filmController.update(film3Updated);

        List<Film> expected = List.of(film1Updated, film2, film3Updated);
        List<Film> actual = filmController.findAll();

        Assertions.assertEquals(expected, actual);
        Assertions.assertThrows(NotFoundException.class, () -> filmController.update(filmWithWrongId));
    }
}
