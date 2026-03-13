package movie.rw.MovieManagementSystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import movie.rw.MovieManagementSystem.model.Director;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {

    // ── Explicit Pagination & Sorting ─────────────────────────────────────────
    Page<Director> findAll(Pageable pageable);

    List<Director> findAll(Sort sort);

    // ── Case-insensitive partial search on firstName or lastName ──────────────
    List<Director> findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(
            String firstName, String lastName);

    Page<Director> findByFirstNameIgnoreCaseContaining(String firstName, Pageable pageable);

    Page<Director> findByLastNameIgnoreCaseContaining(String lastName, Pageable pageable);

    // ── Filter by nationality ─────────────────────────────────────────────────
    List<Director> findByNationality(String nationality);

    Page<Director> findByNationalityIgnoreCaseContaining(String nationality, Pageable pageable);

    // ── Lookup by name combo ──────────────────────────────────────────────────
    Optional<Director> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    // ── existsBy ──────────────────────────────────────────────────────────────
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
}
