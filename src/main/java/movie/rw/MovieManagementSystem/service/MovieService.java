package movie.rw.MovieManagementSystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import movie.rw.MovieManagementSystem.model.Movie;
import movie.rw.MovieManagementSystem.repository.MovieRepository;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    // ── CRUD ──────────────────────────────────────────────────────────────────

    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    public Movie update(Long id, Movie movieDetails) {
        return movieRepository.findById(id)
                .map(movie -> {
                    movie.setTitle(movieDetails.getTitle());
                    movie.setReleaseYear(movieDetails.getReleaseYear());
                    movie.setGenre(movieDetails.getGenre());
                    movie.setRating(movieDetails.getRating());
                    movie.setDirector(movieDetails.getDirector());
                    movie.setActors(movieDetails.getActors());
                    return movieRepository.save(movie);
                })
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
    }

    public void deleteById(Long id) {
        movieRepository.deleteById(id);
    }

    // ── Pagination & Sorting ──────────────────────────────────────────────────

    public Page<Movie> findAll(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public List<Movie> findAll(Sort sort) {
        return movieRepository.findAll(sort);
    }

    // ── Search ────────────────────────────────────────────────────────────────

    public List<Movie> findByTitleIgnoreCaseContainingOrGenreIgnoreCaseContaining(
            String title, String genre) {
        return movieRepository.findByTitleIgnoreCaseContainingOrGenreIgnoreCaseContaining(title, genre);
    }

    public Page<Movie> findByTitleIgnoreCaseContaining(String title, Pageable pageable) {
        return movieRepository.findByTitleIgnoreCaseContaining(title, pageable);
    }

    public Page<Movie> findByGenreIgnoreCaseContaining(String genre, Pageable pageable) {
        return movieRepository.findByGenreIgnoreCaseContaining(genre, pageable);
    }

    // ── Filters ───────────────────────────────────────────────────────────────

    public List<Movie> findByGenre(String genre) {
        return movieRepository.findByGenre(genre);
    }

    public Page<Movie> findByGenre(String genre, Pageable pageable) {
        return movieRepository.findByGenre(genre, pageable);
    }

    public List<Movie> findByReleaseYear(Integer releaseYear) {
        return movieRepository.findByReleaseYear(releaseYear);
    }

    public Page<Movie> findByReleaseYear(Integer releaseYear, Pageable pageable) {
        return movieRepository.findByReleaseYear(releaseYear, pageable);
    }

    public List<Movie> findByDirectorId(Long directorId) {
        return movieRepository.findByDirectorId(directorId);
    }

    public Page<Movie> findByDirectorId(Long directorId, Pageable pageable) {
        return movieRepository.findByDirectorId(directorId, pageable);
    }

    // ── Existence check ───────────────────────────────────────────────────────

    public boolean existsByTitle(String title) {
        return movieRepository.existsByTitle(title);
    }
}
