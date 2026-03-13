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
import movie.rw.MovieManagementSystem.model.Location;
import movie.rw.MovieManagementSystem.model.User;
import movie.rw.MovieManagementSystem.service.LocationService;
import movie.rw.MovieManagementSystem.service.UserService;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserService userService;

    // ── CRUD ──────────────────────────────────────────────────────────────────

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveLocation(@RequestBody Location location) {
        try {
            // Resolve parent entity by ID before saving
            if (location.getParent() != null && location.getParent().getId() != null) {
                Optional<Location> parentLocation = locationService.findById(location.getParent().getId());
                if (parentLocation.isPresent()) {
                    location.setParent(parentLocation.get());
                } else {
                    return new ResponseEntity<>(
                            "Parent location not found with id: " + location.getParent().getId(),
                            HttpStatus.BAD_REQUEST);
                }
            }
            Location savedLocation = locationService.save(location);
            return new ResponseEntity<>(savedLocation, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating location: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Location location) {
        try {
            if (location.getParent() != null && location.getParent().getId() != null) {
                Optional<Location> parentLocation = locationService.findById(location.getParent().getId());
                if (parentLocation.isPresent()) {
                    location.setParent(parentLocation.get());
                } else {
                    return new ResponseEntity<>(
                            "Parent location not found with id: " + location.getParent().getId(),
                            HttpStatus.BAD_REQUEST);
                }
            } else {
                location.setParent(null);
            }
            Location updatedLocation = locationService.update(id, location);
            return new ResponseEntity<>(updatedLocation, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Cannot update location: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating location: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Location>> findAll() {
        return new ResponseEntity<>(locationService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Location> location = locationService.findById(id);
        if (location.isPresent()) {
            return new ResponseEntity<>(location.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Location not found with id: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            locationService.deleteById(id);
            return new ResponseEntity<>("Location deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting location: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ── Pagination & Sorting ──────────────────────────────────────────────────

    @GetMapping(value = "/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Location>> findAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(locationService.findAll(pageable), HttpStatus.OK);
    }

    // ── Search ────────────────────────────────────────────────────────────────

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Location>> search(@RequestParam String searchTerm) {
        List<Location> locations = locationService
                .findByNameIgnoreCaseContainingOrCodeIgnoreCaseContaining(searchTerm, searchTerm);
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    @GetMapping(value = "/search/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Location>> searchByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                locationService.findByNameIgnoreCaseContaining(name, PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @GetMapping(value = "/search/code", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Location>> searchByCode(
            @RequestParam String code,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                locationService.findByCodeIgnoreCaseContaining(code, PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    // ── Filters ───────────────────────────────────────────────────────────────

    /** GET /api/locations/type/{type} — type values: PROVINCE, DISTRICT, SECTOR */
    @GetMapping(value = "/type/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Location>> findByType(@PathVariable ELocationType type) {
        return new ResponseEntity<>(locationService.findByType(type), HttpStatus.OK);
    }

    @GetMapping(value = "/type/{type}/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Location>> findByTypePaged(
            @PathVariable ELocationType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                locationService.findByType(type, PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    /** GET /api/locations/provinces — all top-level locations (parent is null) */
    @GetMapping(value = "/provinces", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Location>> findProvinces() {
        return new ResponseEntity<>(locationService.findByParentIsNull(), HttpStatus.OK);
    }

    /**
     * GET /api/locations/{parentId}/children — districts under a province, sectors
     * under a district
     */
    @GetMapping(value = "/{parentId}/children", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Location>> findChildren(@PathVariable Long parentId) {
        return new ResponseEntity<>(locationService.findByParentId(parentId), HttpStatus.OK);
    }

    // ── Existence checks ──────────────────────────────────────────────────────

    @GetMapping(value = "/exists/code/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> existsByCode(@PathVariable String code) {
        return new ResponseEntity<>(locationService.existsByCode(code), HttpStatus.OK);
    }

    @GetMapping(value = "/exists/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> existsByName(@PathVariable String name) {
        return new ResponseEntity<>(locationService.existsByName(name), HttpStatus.OK);
    }

    // ── Users by Location ─────────────────────────────────────────────────────

    @GetMapping("/{locationId}/users")
    public ResponseEntity<?> getUsersByLocation(@PathVariable Long locationId) {
        try {
            Optional<Location> location = locationService.findById(locationId);
            if (location.isEmpty()) {
                return new ResponseEntity<>("Location not found", HttpStatus.NOT_FOUND);
            }
            List<User> users = userService.findByLocationId(locationId);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving users", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{locationId}/users/paginated")
    public ResponseEntity<?> getUsersByLocationPaginated(
            @PathVariable Long locationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Optional<Location> location = locationService.findById(locationId);
            if (location.isEmpty()) {
                return new ResponseEntity<>("Location not found", HttpStatus.NOT_FOUND);
            }
            Pageable pageable = PageRequest.of(page, size);
            Page<User> users = userService.findByLocationId(locationId, pageable);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving users by locationId", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
