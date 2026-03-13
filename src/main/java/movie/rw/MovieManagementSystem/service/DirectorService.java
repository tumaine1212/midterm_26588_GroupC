package movie.rw.MovieManagementSystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import movie.rw.MovieManagementSystem.model.Director;
import movie.rw.MovieManagementSystem.repository.DirectorRepository;

@Service
public class DirectorService {

    @Autowired
    private DirectorRepository directorRepository;

    // ── CRUD ──────────────────────────────────────────────────────────────────

    public Director save(Director director) {
        return directorRepository.save(director);
    }

    public List<Director> findAll() {
        return directorRepository.findAll();
    }

    public Optional<Director> findById(Long id) {
        return directorRepository.findById(id);
    }

    public Director update(Long id, Director directorDetails) {
        return directorRepository.findById(id)
                .map(director -> {
                    director.setFirstName(directorDetails.getFirstName());
                    director.setLastName(directorDetails.getLastName());
                    director.setNationality(directorDetails.getNationality());
                    return directorRepository.save(director);
                })
                .orElseThrow(() -> new RuntimeException("Director not found with id: " + id));
    }

    public void deleteById(Long id) {
        directorRepository.deleteById(id);
    }

    // ── Pagination & Sorting ──────────────────────────────────────────────────

    public Page<Director> findAll(Pageable pageable) {
        return directorRepository.findAll(pageable);
    }

    public List<Director> findAll(Sort sort) {
        return directorRepository.findAll(sort);
    }

    // ── Search ────────────────────────────────────────────────────────────────

    public List<Director> findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(
            String firstName, String lastName) {
        return directorRepository.findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(
                firstName, lastName);
    }

    public Page<Director> findByFirstNameIgnoreCaseContaining(String firstName, Pageable pageable) {
        return directorRepository.findByFirstNameIgnoreCaseContaining(firstName, pageable);
    }

    public Page<Director> findByLastNameIgnoreCaseContaining(String lastName, Pageable pageable) {
        return directorRepository.findByLastNameIgnoreCaseContaining(lastName, pageable);
    }

    // ── Filters ───────────────────────────────────────────────────────────────

    public List<Director> findByNationality(String nationality) {
        return directorRepository.findByNationality(nationality);
    }

    public Page<Director> findByNationalityIgnoreCaseContaining(String nationality, Pageable pageable) {
        return directorRepository.findByNationalityIgnoreCaseContaining(nationality, pageable);
    }

    public Optional<Director> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(
            String firstName, String lastName) {
        return directorRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);
    }

    // ── Existence check ───────────────────────────────────────────────────────

    public boolean existsByFirstNameAndLastName(String firstName, String lastName) {
        return directorRepository.existsByFirstNameAndLastName(firstName, lastName);
    }
}
