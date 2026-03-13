package movie.rw.MovieManagementSystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import movie.rw.MovieManagementSystem.model.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {

    // ── Explicit Pagination & Sorting ─────────────────────────────────────────
    Page<Actor> findAll(Pageable pageable);

    List<Actor> findAll(Sort sort);

    // ── Case-insensitive partial search on firstName or lastName ──────────────
    List<Actor> findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(
            String firstName, String lastName);

    Page<Actor> findByFirstNameIgnoreCaseContaining(String firstName, Pageable pageable);

    Page<Actor> findByLastNameIgnoreCaseContaining(String lastName, Pageable pageable);

    // ── Filter by nationality ─────────────────────────────────────────────────
    List<Actor> findByNationality(String nationality);

    Page<Actor> findByNationalityIgnoreCaseContaining(String nationality, Pageable pageable);

    // ── Lookup by name combo ──────────────────────────────────────────────────
    Optional<Actor> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    // ── existsBy ──────────────────────────────────────────────────────────────
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
}
