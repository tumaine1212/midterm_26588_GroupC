package movie.rw.MovieManagementSystem.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Represents a movie Director.
 *
 * ONE-TO-MANY relationship with Movie:
 *   One Director can direct MANY Movies.
 *   The 'director_id' foreign key lives on the Movie table (the "many" side).
 *   'mappedBy = "director"' tells JPA that Movie owns this relationship.
 */
@Entity
@Table(name = "director")
public class Director {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "nationality")
    private String nationality;

    /**
     * One Director → Many Movies.
     * 'mappedBy' points to the field name in Movie that owns the FK.
     * CascadeType.ALL: operations on Director cascade to their Movies.
     * @JsonIgnore prevents infinite recursion during JSON serialization.
     */
    @OneToMany(mappedBy = "director", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Movie> movies = new ArrayList<>();

    public Director() {
    }

    public Director(String firstName, String lastName, String nationality) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
    }

    // ── Getters & Setters ──────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public List<Movie> getMovies() { return movies; }
    public void setMovies(List<Movie> movies) { this.movies = movies; }
}
