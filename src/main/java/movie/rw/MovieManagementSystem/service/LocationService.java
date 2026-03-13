package movie.rw.MovieManagementSystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import movie.rw.MovieManagementSystem.model.ELocationType;
import movie.rw.MovieManagementSystem.model.Location;
import movie.rw.MovieManagementSystem.repository.LocationRepository;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    // ── CRUD ──────────────────────────────────────────────────────────────────

    public Location save(Location location) {
        return locationRepository.save(location);
    }

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public Optional<Location> findById(Long id) {
        return locationRepository.findById(id);
    }

    public Location update(Long id, Location locationDetails) {
        return locationRepository.findById(id)
                .map(location -> {
                    location.setName(locationDetails.getName());
                    location.setCode(locationDetails.getCode());
                    location.setType(locationDetails.getType());
                    location.setParent(locationDetails.getParent());
                    return locationRepository.save(location);
                })
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
    }

    public void deleteById(Long id) {
        locationRepository.deleteById(id);
    }

    // ── Pagination & Sorting ──────────────────────────────────────────────────

    public Page<Location> findAll(Pageable pageable) {
        return locationRepository.findAll(pageable);
    }

    public List<Location> findAll(Sort sort) {
        return locationRepository.findAll(sort);
    }

    // ── Search ────────────────────────────────────────────────────────────────

    public List<Location> findByNameIgnoreCaseContainingOrCodeIgnoreCaseContaining(String name, String code) {
        return locationRepository.findByNameIgnoreCaseContainingOrCodeIgnoreCaseContaining(name, code);
    }

    public Page<Location> findByNameIgnoreCaseContaining(String name, Pageable pageable) {
        return locationRepository.findByNameIgnoreCaseContaining(name, pageable);
    }

    public Page<Location> findByCodeIgnoreCaseContaining(String code, Pageable pageable) {
        return locationRepository.findByCodeIgnoreCaseContaining(code, pageable);
    }

    // ── Filters ───────────────────────────────────────────────────────────────

    public List<Location> findByType(ELocationType type) {
        return locationRepository.findByType(type);
    }

    public Page<Location> findByType(ELocationType type, Pageable pageable) {
        return locationRepository.findByType(type, pageable);
    }

    public List<Location> findByParentId(Long parentId) {
        return locationRepository.findByParentId(parentId);
    }

    public List<Location> findByParentIsNull() {
        return locationRepository.findByParentIsNull();
    }

    // ── Existence checks ──────────────────────────────────────────────────────

    public boolean existsByCode(String code) {
        return locationRepository.existsByCode(code);
    }

    public boolean existsByName(String name) {
        return locationRepository.existsByName(name);
    }
}
