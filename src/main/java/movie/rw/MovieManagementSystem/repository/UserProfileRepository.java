package movie.rw.MovieManagementSystem.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import movie.rw.MovieManagementSystem.model.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    // ── Explicit Pagination & Sorting ─────────────────────────────────────────
    Page<UserProfile> findAll(Pageable pageable);

    List<UserProfile> findAll(Sort sort);

    // ── Lookup ────────────────────────────────────────────────────────────────
    Optional<UserProfile> findByPhoneNumber(String phoneNumber);

    // ── existsBy ──────────────────────────────────────────────────────────────
    boolean existsByPhoneNumber(String phoneNumber);
}
