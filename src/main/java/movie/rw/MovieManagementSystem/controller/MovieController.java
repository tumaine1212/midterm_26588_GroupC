package movie.rw.MovieManagementSystem.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import movie.rw.MovieManagementSystem.model.Movie;
import movie.rw.MovieManagementSystem.service.MovieService;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    // ── CRUD ──────────────────────────────────────────────────────────────────

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody Movie movie) {
        try {
            Movie savedMovie = movieService.save(movie);
            return new ResponseEntity<>(savedMovie, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating movie: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Movie movieDetails) {
        try {
            Movie updatedMovie = movieService.update(id, movieDetails);
            return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Cannot update movie: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating movie: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> findAll() {
        return new ResponseEntity<>(movieService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Movie> movie = movieService.findById(id);
        if (movie.isPresent()) {
            return new ResponseEntity<>(movie.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Movie not found with id: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            movieService.deleteById(id);
            return new ResponseEntity<>("Movie deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting movie: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ── Pagination ────────────────────────────────────────────────────────────

    @GetMapping(value = "/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Movie>> findAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(movieService.findAll(pageable), HttpStatus.OK);
    }

    // ── Search ────────────────────────────────────────────────────────────────

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> search(@RequestParam String searchTerm) {
        List<Movie> movies = movieService
                .findByTitleIgnoreCaseContainingOrGenreIgnoreCaseContaining(searchTerm, searchTerm);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping(value = "/search/title", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Movie>> searchByTitle(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                movieService.findByTitleIgnoreCaseContaining(title, PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @GetMapping(value = "/search/genre", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Movie>> searchByGenre(
            @RequestParam String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                movieService.findByGenreIgnoreCaseContaining(genre, PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    // ── Filters ───────────────────────────────────────────────────────────────

    @GetMapping(value = "/genre/{genre}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> findByGenre(@PathVariable String genre) {
        return new ResponseEntity<>(movieService.findByGenre(genre), HttpStatus.OK);
    }

    @GetMapping(value = "/genre/{genre}/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Movie>> findByGenrePaged(
            @PathVariable String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                movieService.findByGenre(genre, PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @GetMapping(value = "/year/{releaseYear}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> findByYear(@PathVariable Integer releaseYear) {
        return new ResponseEntity<>(movieService.findByReleaseYear(releaseYear), HttpStatus.OK);
    }

    @GetMapping(value = "/year/{releaseYear}/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Movie>> findByYearPaged(
            @PathVariable Integer releaseYear,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                movieService.findByReleaseYear(releaseYear, PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @GetMapping(value = "/director/{directorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> findByDirector(@PathVariable Long directorId) {
        return new ResponseEntity<>(movieService.findByDirectorId(directorId), HttpStatus.OK);
    }

    @GetMapping(value = "/director/{directorId}/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Movie>> findByDirectorPaged(
            @PathVariable Long directorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                movieService.findByDirectorId(directorId, PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    // ── Existence check ───────────────────────────────────────────────────────

    @GetMapping(value = "/exists/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> existsByTitle(@PathVariable String title) {
        return new ResponseEntity<>(movieService.existsByTitle(title), HttpStatus.OK);
    }
}
