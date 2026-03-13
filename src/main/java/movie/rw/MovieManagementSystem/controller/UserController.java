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

import movie.rw.MovieManagementSystem.model.ELocationType;
import movie.rw.MovieManagementSystem.model.EUserRole;
import movie.rw.MovieManagementSystem.model.User;
import movie.rw.MovieManagementSystem.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ── CRUD ──────────────────────────────────────────────────────────────────

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody User user) {
        try {
            User savedUser = userService.save(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating user: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            User updatedUser = userService.update(id, userDetails);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Cannot update user: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating user: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found with id: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            userService.deleteById(id);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting user: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ── Pagination ────────────────────────────────────────────────────────────

    @GetMapping(value = "/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<User>> findAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(userService.findAll(pageable), HttpStatus.OK);
    }

    // ── Search ────────────────────────────────────────────────────────────────

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> search(@RequestParam String searchTerm) {
        List<User> users = userService
                .findByUsernameIgnoreCaseContainingOrEmailIgnoreCaseContaining(searchTerm, searchTerm);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/search/username", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<User>> searchByUsername(
            @RequestParam String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                userService.findByUsernameIgnoreCaseContaining(username, PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @GetMapping(value = "/search/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<User>> searchByEmail(
            @RequestParam String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                userService.findByEmailIgnoreCaseContaining(email, PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    // ── Filters ───────────────────────────────────────────────────────────────

    /** GET /api/users/role/{role} — values: ADMIN, USER, MODERATOR */
    @GetMapping(value = "/role/{role}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> findByRole(@PathVariable EUserRole role) {
        return new ResponseEntity<>(userService.findByRole(role), HttpStatus.OK);
    }

    @GetMapping(value = "/role/{role}/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<User>> findByRolePaged(
            @PathVariable EUserRole role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                userService.findByRole(role, PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    // ── Existence checks ──────────────────────────────────────────────────────

    @GetMapping(value = "/exists/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> existsByUsername(@PathVariable String username) {
        return new ResponseEntity<>(userService.existsByUsername(username), HttpStatus.OK);
    }

    @GetMapping(value = "/exists/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        return new ResponseEntity<>(userService.existsByEmail(email), HttpStatus.OK);
    }

    // ── Province / Hierarchy Queries (assessment requirement #8) ─────────────

    /** GET /api/users/by-province/code/{code} */
    @GetMapping(value = "/by-province/code/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByProvinceCode(@PathVariable String code) {
        try {
            List<User> users = userService.findByProvinceCode(code);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving users by province code: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** GET /api/users/by-province/name/{name} */
    @GetMapping(value = "/by-province/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByProvinceName(@PathVariable String name) {
        try {
            List<User> users = userService.findByProvinceName(name);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving users by province name: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** GET /api/users/by-province/any?code=KG&name=Kigali */
    @GetMapping(value = "/by-province/any", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByProvinceAny(
            @RequestParam(required = false, defaultValue = "") String code,
            @RequestParam(required = false, defaultValue = "") String name) {
        try {
            List<User> users = userService.findByProvinceCodeOrProvinceName(code, name);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving users: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /api/users/by-location-hierarchy?locType=PROVINCE&locValue=Kigali
     * Walks the Location tree up to 3 levels deep.
     * locType values: PROVINCE, DISTRICT, SECTOR
     */
    @GetMapping(value = "/by-location-hierarchy", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByLocationHierarchy(
            @RequestParam ELocationType locType,
            @RequestParam String locValue) {
        try {
            List<User> users = userService.findUsersByLocationHierarchy(locType, locValue);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving users by hierarchy: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
