package movie.rw.MovieManagementSystem.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import movie.rw.MovieManagementSystem.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // ── Explicit Pagination & Sorting ─────────────────────────────────────────
    Page<Movie> findAll(Pageable pageable);

    List<Movie> findAll(Sort sort);

    // ── Case-insensitive partial search on title or genre ─────────────────────
    List<Movie> findByTitleIgnoreCaseContainingOrGenreIgnoreCaseContaining(
            String title, String genre);

    Page<Movie> findByTitleIgnoreCaseContaining(String title, Pageable pageable);

    Page<Movie> findByGenreIgnoreCaseContaining(String genre, Pageable pageable);

    // ── Filter by genre (exact) ───────────────────────────────────────────────
    List<Movie> findByGenre(String genre);

    Page<Movie> findByGenre(String genre, Pageable pageable);

    // ── Filter by release year ────────────────────────────────────────────────
    List<Movie> findByReleaseYear(Integer releaseYear);

    Page<Movie> findByReleaseYear(Integer releaseYear, Pageable pageable);

    // ── Filter by director ────────────────────────────────────────────────────
    List<Movie> findByDirectorId(Long directorId);

    Page<Movie> findByDirectorId(Long directorId, Pageable pageable);

    // ── existsBy ──────────────────────────────────────────────────────────────
    boolean existsByTitle(String title);
}
