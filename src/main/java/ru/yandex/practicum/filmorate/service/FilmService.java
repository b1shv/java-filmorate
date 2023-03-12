package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage storage;
    private final UserService userService;
    private static final LocalDate FIRST_FILM_RELEASE_DATE = (LocalDate.of(1895, 12, 28));

    @Autowired
    public FilmService(FilmStorage storage, UserService userService) {
        this.storage = storage;
        this.userService = userService;
    }

    public List<Film> getFilms() {
        return storage.getFilms();
    }

    public List<Film> getMostPopularFilms(int count) {
        return storage.getFilms().stream()
                .sorted(Comparator.comparingInt(Film::getLikesNumber).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilmById(int id) {
        storage.checkFilmId(id);
        return storage.getFilmById(id);
    }

    public Film addFilm(Film film) {
        validateReleaseDate(film);
        log.debug("POST request handled: new film added");
        return storage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        validateReleaseDate(film);
        storage.checkFilmId(film.getId());

        log.debug(String.format("PUT request handled: film %d updated", film.getId()));
        return storage.updateFilm(film);
    }

    public void deleteFilm(int id) {
        storage.checkFilmId(id);
        storage.deleteFilm(id);
    }

    public void addLike(int filmId, int userId) {
        storage.checkFilmId(filmId);
        userService.checkUserId(userId);

        storage.getFilmById(filmId).addLike(userId);
    }

    public void deleteLike(int filmId, int userId) {
        storage.checkFilmId(filmId);
        userService.checkUserId(userId);

        if (!storage.getFilmById(filmId).getLikes().contains(userId)) {
            throw new NotFoundException(String.format("Film %d doesn't have likes from user %d", filmId, userId));
        }
        storage.getFilmById(filmId).deleteLike(userId);
    }

    private void validateReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(FIRST_FILM_RELEASE_DATE)) {
            throw new ValidationException("Validation failed: Incorrect release date");
        }
    }
}
