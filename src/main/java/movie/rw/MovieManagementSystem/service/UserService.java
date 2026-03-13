package movie.rw.MovieManagementSystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import movie.rw.MovieManagementSystem.model.ELocationType;
import movie.rw.MovieManagementSystem.model.EUserRole;
import movie.rw.MovieManagementSystem.model.User;
import movie.rw.MovieManagementSystem.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ── CRUD ──────────────────────────────────────────────────────────────────

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User update(Long id, User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(userDetails.getUsername());
                    user.setEmail(userDetails.getEmail());
                    user.setPassword(userDetails.getPassword());
                    user.setRole(userDetails.getRole());
                    user.setLocation(userDetails.getLocation());
                    user.setUserProfile(userDetails.getUserProfile());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    // ── Pagination & Sorting ──────────────────────────────────────────────────

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public List<User> findAll(Sort sort) {
        return userRepository.findAll(sort);
    }

    // ── Search ────────────────────────────────────────────────────────────────

    public List<User> findByUsernameIgnoreCaseContainingOrEmailIgnoreCaseContaining(
            String username, String email) {
        return userRepository.findByUsernameIgnoreCaseContainingOrEmailIgnoreCaseContaining(username, email);
    }

    public Page<User> findByUsernameIgnoreCaseContaining(String username, Pageable pageable) {
        return userRepository.findByUsernameIgnoreCaseContaining(username, pageable);
    }

    public Page<User> findByEmailIgnoreCaseContaining(String email, Pageable pageable) {
        return userRepository.findByEmailIgnoreCaseContaining(email, pageable);
    }

    // ── Filters ───────────────────────────────────────────────────────────────

    public List<User> findByRole(EUserRole role) {
        return userRepository.findByRole(role);
    }

    public Page<User> findByRole(EUserRole role, Pageable pageable) {
        return userRepository.findByRole(role, pageable);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findByLocationId(Long locationId) {
        return userRepository.findByLocationId(locationId);
    }

    public Page<User> findByLocationId(Long locationId, Pageable pageable) {
        return userRepository.findByLocationId(locationId, pageable);
    }

    // ── Existence checks ──────────────────────────────────────────────────────

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // ── Province / Location Hierarchy queries ─────────────────────────────────

    public List<User> findByProvinceCode(String provinceCode) {
        return userRepository.findByProvinceCode(provinceCode);
    }

    public List<User> findByProvinceName(String provinceName) {
        return userRepository.findByProvinceName(provinceName);
    }

    public List<User> findByProvinceCodeOrProvinceName(String code, String name) {
        return userRepository.findByProvinceCodeOrProvinceName(code, name);
    }

    public Page<User> findByProvinceCodePaged(String provinceCode, Pageable pageable) {
        return userRepository.findByProvinceCodePaged(provinceCode, pageable);
    }

    public List<User> findUsersByLocationHierarchy(ELocationType locType, String locValue) {
        return userRepository.findUsersByLocationHierarchy(locType, locValue);
    }
}
