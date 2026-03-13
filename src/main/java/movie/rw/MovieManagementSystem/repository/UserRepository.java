package movie.rw.MovieManagementSystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import movie.rw.MovieManagementSystem.model.ELocationType;
import movie.rw.MovieManagementSystem.model.EUserRole;
import movie.rw.MovieManagementSystem.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

        // ── Explicit Pagination & Sorting ─────────────────────────────────────────
        Page<User> findAll(Pageable pageable);

        List<User> findAll(Sort sort);

        // ── Combined search by username OR email (case-insensitive) ───────────────
        List<User> findByUsernameIgnoreCaseContainingOrEmailIgnoreCaseContaining(
                        String username, String email);

        Page<User> findByUsernameIgnoreCaseContaining(String username, Pageable pageable);

        Page<User> findByEmailIgnoreCaseContaining(String email, Pageable pageable);

        // ── Filter by role ────────────────────────────────────────────────────────
        List<User> findByRole(EUserRole role);

        Page<User> findByRole(EUserRole role, Pageable pageable);

        // ── Lookup by unique fields ───────────────────────────────────────────────
        Optional<User> findByUsername(String username);

        Optional<User> findByEmail(String email);

        // ── existsBy ──────────────────────────────────────────────────────────────
        boolean existsByUsername(String username);

        boolean existsByEmail(String email);

        // ── Filter by direct location FK ──────────────────────────────────────────
        List<User> findByLocationId(Long locationId);

        Page<User> findByLocationId(Long locationId, Pageable pageable);

        // ── Province queries (assessment requirement #8) ──────────────────────────

        /**
         * Retrieve all users whose DIRECT location matches the given province code.
         * JPQL: User → location → code
         */
        @Query("SELECT u FROM User u WHERE u.location.code = :provinceCode")
        List<User> findByProvinceCode(@Param("provinceCode") String provinceCode);

        /**
         * Retrieve all users whose DIRECT location name matches (case-insensitive).
         * JPQL: User → location → name (LOWER for case-insensitivity)
         */
        @Query("SELECT u FROM User u WHERE LOWER(u.location.name) = LOWER(:provinceName)")
        List<User> findByProvinceName(@Param("provinceName") String provinceName);

        /**
         * Retrieve users by province CODE OR NAME in a single query.
         */
        @Query("SELECT u FROM User u WHERE u.location.code = :code " +
                        "OR LOWER(u.location.name) = LOWER(:name)")
        List<User> findByProvinceCodeOrProvinceName(
                        @Param("code") String code,
                        @Param("name") String name);

        /** Paginated province-code query. */
        @Query("SELECT u FROM User u WHERE u.location.code = :provinceCode")
        Page<User> findByProvinceCodePaged(
                        @Param("provinceCode") String provinceCode,
                        Pageable pageable);

        /**
         * Hierarchy-aware location search.
         *
         * Traverses up to 4 levels of the self-referencing Location tree:
         * Level 0: user's direct location
         * Level 1: parent (e.g., District → Province)
         * Level 2: parent's parent
         * Level 3: parent's parent's parent
         *
         * Matches if ANY ancestor at the given locType has a name or code
         * that contains locValue (case-insensitive, partial match).
         *
         * Example: find all users in PROVINCE "Kigali", even when the user's
         * direct location is a SECTOR inside Kigali.
         */
        @Query("SELECT u FROM User u WHERE " +
                        "(u.location.type = :locType AND " +
                        "(LOWER(u.location.name) LIKE LOWER(CONCAT('%', :locValue, '%')) " +
                        "OR LOWER(u.location.code) LIKE LOWER(CONCAT('%', :locValue, '%')))) " +
                        "OR (u.location.parent.type = :locType AND " +
                        "(LOWER(u.location.parent.name) LIKE LOWER(CONCAT('%', :locValue, '%')) " +
                        "OR LOWER(u.location.parent.code) LIKE LOWER(CONCAT('%', :locValue, '%')))) " +
                        "OR (u.location.parent.parent.type = :locType AND " +
                        "(LOWER(u.location.parent.parent.name) LIKE LOWER(CONCAT('%', :locValue, '%')) " +
                        "OR LOWER(u.location.parent.parent.code) LIKE LOWER(CONCAT('%', :locValue, '%')))) " +
                        "OR (u.location.parent.parent.parent.type = :locType AND " +
                        "(LOWER(u.location.parent.parent.parent.name) LIKE LOWER(CONCAT('%', :locValue, '%')) " +
                        "OR LOWER(u.location.parent.parent.parent.code) LIKE LOWER(CONCAT('%', :locValue, '%'))))")
        List<User> findUsersByLocationHierarchy(
                        @Param("locType") ELocationType locType,
                        @Param("locValue") String locValue);
}
