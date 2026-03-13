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

import movie.rw.MovieManagementSystem.model.Actor;
import movie.rw.MovieManagementSystem.service.ActorService;

@RestController
@RequestMapping("/api/actors")
public class ActorController {

    @Autowired
    private ActorService actorService;

    // ── CRUD ──────────────────────────────────────────────────────────────────

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody Actor actor) {
        try {
            Actor savedActor = actorService.save(actor);
            return new ResponseEntity<>(savedActor, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating actor: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Actor actorDetails) {
        try {
            Actor updatedActor = actorService.update(id, actorDetails);
            return new ResponseEntity<>(updatedActor, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Cannot update actor: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating actor: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Actor>> findAll() {
        return new ResponseEntity<>(actorService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Actor> actor = actorService.findById(id);
        if (actor.isPresent()) {
            return new ResponseEntity<>(actor.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Actor not found with id: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            actorService.deleteById(id);
            return new ResponseEntity<>("Actor deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting actor: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ── Pagination ────────────────────────────────────────────────────────────

    @GetMapping(value = "/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Actor>> findAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(actorService.findAll(pageable), HttpStatus.OK);
    }

    // ── Search ────────────────────────────────────────────────────────────────

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Actor>> search(@RequestParam String searchTerm) {
        List<Actor> actors = actorService
                .findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(searchTerm, searchTerm);
        return new ResponseEntity<>(actors, HttpStatus.OK);
    }

    @GetMapping(value = "/search/firstname", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Actor>> searchByFirstName(
            @RequestParam String firstName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                actorService.findByFirstNameIgnoreCaseContaining(firstName, PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    @GetMapping(value = "/search/lastname", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Actor>> searchByLastName(
            @RequestParam String lastName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                actorService.findByLastNameIgnoreCaseContaining(lastName, PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    // ── Filters ───────────────────────────────────────────────────────────────

    @GetMapping(value = "/nationality/{nationality}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Actor>> findByNationality(@PathVariable String nationality) {
        return new ResponseEntity<>(actorService.findByNationality(nationality), HttpStatus.OK);
    }

    @GetMapping(value = "/nationality/{nationality}/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Actor>> findByNationalityPaged(
            @PathVariable String nationality,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                actorService.findByNationalityIgnoreCaseContaining(nationality, PageRequest.of(page, size)),
                HttpStatus.OK);
    }

    // ── Existence check ───────────────────────────────────────────────────────

    @GetMapping(value = "/exists", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> existsByName(
            @RequestParam String firstName, @RequestParam String lastName) {
        return new ResponseEntity<>(
                actorService.existsByFirstNameAndLastName(firstName, lastName),
                HttpStatus.OK);
    }
}
