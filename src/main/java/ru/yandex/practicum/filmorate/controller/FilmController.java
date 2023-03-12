package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @GetMapping
    public List<Film> findAll() {
        return service.getFilms();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable int id) {
        return service.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> findMostPopular(@RequestParam(name = "count", defaultValue = "10") int count) {
        return service.getMostPopularFilms(count);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return service.addFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return service.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        service.addLike(id, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        service.deleteFilm(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        service.deleteLike(id, userId);
    }

}
