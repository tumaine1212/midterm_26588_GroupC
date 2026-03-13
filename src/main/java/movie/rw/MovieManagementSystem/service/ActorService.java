package movie.rw.MovieManagementSystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import movie.rw.MovieManagementSystem.model.Actor;
import movie.rw.MovieManagementSystem.repository.ActorRepository;

@Service
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    // ── CRUD ──────────────────────────────────────────────────────────────────

    public Actor save(Actor actor) {
        return actorRepository.save(actor);
    }

    public List<Actor> findAll() {
        return actorRepository.findAll();
    }

    public Optional<Actor> findById(Long id) {
        return actorRepository.findById(id);
    }

    public Actor update(Long id, Actor actorDetails) {
        return actorRepository.findById(id)
                .map(actor -> {
                    actor.setFirstName(actorDetails.getFirstName());
                    actor.setLastName(actorDetails.getLastName());
                    actor.setNationality(actorDetails.getNationality());
                    return actorRepository.save(actor);
                })
                .orElseThrow(() -> new RuntimeException("Actor not found with id: " + id));
    }

    public void deleteById(Long id) {
        actorRepository.deleteById(id);
    }

    // ── Pagination & Sorting ──────────────────────────────────────────────────

    public Page<Actor> findAll(Pageable pageable) {
        return actorRepository.findAll(pageable);
    }

    public List<Actor> findAll(Sort sort) {
        return actorRepository.findAll(sort);
    }

    // ── Search ────────────────────────────────────────────────────────────────

    public List<Actor> findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(
            String firstName, String lastName) {
        return actorRepository.findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(
                firstName, lastName);
    }

    public Page<Actor> findByFirstNameIgnoreCaseContaining(String firstName, Pageable pageable) {
        return actorRepository.findByFirstNameIgnoreCaseContaining(firstName, pageable);
    }

    public Page<Actor> findByLastNameIgnoreCaseContaining(String lastName, Pageable pageable) {
        return actorRepository.findByLastNameIgnoreCaseContaining(lastName, pageable);
    }

    // ── Filters ───────────────────────────────────────────────────────────────

    public List<Actor> findByNationality(String nationality) {
        return actorRepository.findByNationality(nationality);
    }

    public Page<Actor> findByNationalityIgnoreCaseContaining(String nationality, Pageable pageable) {
        return actorRepository.findByNationalityIgnoreCaseContaining(nationality, pageable);
    }

    public Optional<Actor> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(
            String firstName, String lastName) {
        return actorRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);
    }

    // ── Existence check ───────────────────────────────────────────────────────

    public boolean existsByFirstNameAndLastName(String firstName, String lastName) {
        return actorRepository.existsByFirstNameAndLastName(firstName, lastName);
    }
}
