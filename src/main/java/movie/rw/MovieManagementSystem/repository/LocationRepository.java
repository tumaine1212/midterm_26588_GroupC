package movie.rw.MovieManagementSystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import movie.rw.MovieManagementSystem.model.ELocationType;
import movie.rw.MovieManagementSystem.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    // ── Explicit Pagination & Sorting ─────────────────────────────────────────
    Page<Location> findAll(Pageable pageable);

    List<Location> findAll(Sort sort);

    // ── Case-insensitive partial search on name or code ───────────────────────
    List<Location> findByNameIgnoreCaseContainingOrCodeIgnoreCaseContaining(
            String name, String code);

    Page<Location> findByNameIgnoreCaseContaining(String name, Pageable pageable);

    Page<Location> findByCodeIgnoreCaseContaining(String code, Pageable pageable);

    // ── Filter by location type (PROVINCE / DISTRICT / SECTOR) ───────────────
    List<Location> findByType(ELocationType type);

    Page<Location> findByType(ELocationType type, Pageable pageable);

    // ── Hierarchy navigation ──────────────────────────────────────────────────
    /** All children of a given parent (e.g., all districts under a province). */
    List<Location> findByParentId(Long parentId);

    /** All top-level locations (provinces) — parent is null. */
    List<Location> findByParentIsNull();

    // ── Lookup by unique code ─────────────────────────────────────────────────
    Optional<Location> findByCode(String code);

    // ── existsBy ──────────────────────────────────────────────────────────────
    boolean existsByCode(String code);

    boolean existsByName(String name);
}
