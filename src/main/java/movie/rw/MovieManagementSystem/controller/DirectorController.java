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

import movie.rw.MovieManagementSystem.model.Director;
import movie.rw.MovieManagementSystem.service.DirectorService;

@RestController
@RequestMapping("/api/directors")
public class DirectorController {

    @Autowired
    private DirectorService directorService;

    // ── CRUD ──────────────────────────────────────────────────────────────────

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody Director director) {
        try {
            Director savedDirector = directorService.save(director);
            return new ResponseEntity<>(savedDirector, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating director: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Director directorDetails) {
        try {
            Director updatedDirector = directorService.update(id, directorDetails);
            return new ResponseEntity<>(updatedDirector, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Cannot update director: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating director: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Director>> findAll() {
        return new ResponseEntity<>(directorService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Director> director = directorService.findById(id);
        if (director.isPresent()) {
            return new ResponseEntity<>(director.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Director not found with id: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            directorService.deleteById(id);
            return new ResponseEntity<>("Director deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting director: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ── Pagination ────────────────────────────────────────────────────────────

    @GetMapping(value = "/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Director>> findAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(directorService.findAll(pageable), HttpStatus.OK);
    }

    // ── Search ────────────────────────────────────────────────────────────────

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Director>> search(@RequestParam String searchTerm) {
        List<Director> directors = directorService
                .findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(searchTerm, searchTerm);
        return new ResponseEntity<>(directors, HttpStatus.OK);
    }

    @GetMapping(value = "/search/firstname", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Director>> searchByFirstName(
            @RequestParam String firstName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                directorService.findByFirstNameIgnoreCaseContaining(firstName, PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @GetMapping(value = "/search/lastname", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Director>> searchByLastName(
            @RequestParam String lastName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                directorService.findByLastNameIgnoreCaseContaining(lastName, PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    // ── Filters ───────────────────────────────────────────────────────────────

    @GetMapping(value = "/nationality/{nationality}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Director>> findByNationality(@PathVariable String nationality) {
        return new ResponseEntity<>(directorService.findByNationality(nationality), HttpStatus.OK);
    }

    @GetMapping(value = "/nationality/{nationality}/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Director>> findByNationalityPaged(
            @PathVariable String nationality,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                directorService.findByNationalityIgnoreCaseContaining(nationality, PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    // ── Existence check ───────────────────────────────────────────────────────

    @GetMapping(value = "/exists", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> existsByName(
            @RequestParam String firstName, @RequestParam String lastName) {
        return new ResponseEntity<>(
                directorService.existsByFirstNameAndLastName(firstName, lastName),
                HttpStatus.OK);
    }
}
