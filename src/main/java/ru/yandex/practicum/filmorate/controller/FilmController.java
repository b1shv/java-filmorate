package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int idCounter = 0;
    private final LocalDate FIRST_FILM_RELEASE_DATE = (LocalDate.of(1895, 12, 28));

    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
            validateReleaseDate(film);
            film.setId(++idCounter);
            films.put(film.getId(), film);
            log.debug("POST request handled: new film added");
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        validateReleaseDate(film);
        if (!films.containsKey(film.getId())) {
            log.warn("PUT request with wrong ID");
            throw new NotFoundException();
        }

        films.put(film.getId(), film);
        log.debug("PUT request handled: film updated");
        return film;
    }

    private void validateReleaseDate(Film film) {
        if (film.getReleaseDate() == null) {
            return;
        }
        if (film.getReleaseDate().isBefore(FIRST_FILM_RELEASE_DATE)) {
            log.warn("Validation failed: Incorrect release date");
            throw new ValidationException();
        }
    }
}
