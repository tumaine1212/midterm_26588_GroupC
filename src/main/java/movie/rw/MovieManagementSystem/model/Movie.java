package movie.rw.MovieManagementSystem.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Represents a Movie entity.
 *
 * Relationships:
 * ─────────────────────────────────────────────────────────────
 * 1. MANY-TO-ONE → Director (owning side):
 *    Many movies can be directed by one Director.
 *    The 'director_id' FK lives on the movie table.
 *
 * 2. MANY-TO-MANY ↔ Actor (OWNING side):
 *    One movie can have many actors; one actor appears in many movies.
 *    JPA creates a join table 'movie_actor' with two FK columns:
 *      - movie_id  → references movie.id
 *      - actor_id  → references actor.id
 * ─────────────────────────────────────────────────────────────
 */
@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(name = "genre")
    private String genre;

    @Column(name = "rating")
    private Double rating;

    /**
     * MANY-TO-ONE: Many movies share one Director.
     * 'director_id' FK is stored on this (movie) table.
     */
    @ManyToOne
    @JoinColumn(name = "director_id")
    private Director director;

    /**
     * MANY-TO-MANY (OWNING SIDE):
     * @JoinTable defines the actual join table in the database.
     *   name        = table name in DB
     *   joinColumns = FK back to THIS entity (movie)
     *   inverseJoinColumns = FK to the OTHER entity (actor)
     */
    @ManyToMany
    @JoinTable(
        name = "movie_actor",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private List<Actor> actors = new ArrayList<>();

    public Movie() {
    }

    public Movie(String title, Integer releaseYear, String genre, Double rating, Director director) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.rating = rating;
        this.director = director;
    }

    // ── Getters & Setters ──────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Director getDirector() { return director; }
    public void setDirector(Director director) { this.director = director; }

    public List<Actor> getActors() { return actors; }
    public void setActors(List<Actor> actors) { this.actors = actors; }
}
