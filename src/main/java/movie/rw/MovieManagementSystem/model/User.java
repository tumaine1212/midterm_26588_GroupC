package movie.rw.MovieManagementSystem.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Represents an application User.
 *
 * Relationships:
 * ─────────────────────────────────────────────────────────────
 * 1. ONE-TO-ONE → UserProfile (OWNING SIDE):
 *    Each user has one profile. The 'user_profile_id' FK lives on this table.
 *    CascadeType.ALL: saving/deleting a User cascades to its UserProfile.
 *
 * 2. MANY-TO-ONE → Location (Province):
 *    Many users can live in the same province.
 *    The 'location_id' FK lives on this (user) table.
 *    This enables the requirement: "retrieve users by province code or name."
 * ─────────────────────────────────────────────────────────────
 */
@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Enum stored as STRING ("ADMIN", "USER", "MODERATOR").
     */
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private EUserRole role;

    /**
     * ONE-TO-ONE (OWNING SIDE):
     * @JoinColumn stores 'user_profile_id' as a FK column in app_user table.
     * CascadeType.ALL ensures profile is persisted/removed with the user.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_profile_id", unique = true)
    private UserProfile userProfile;

    /**
     * MANY-TO-ONE → Location (Province level):
     * Many users can share one province.
     * 'location_id' FK stored on this table.
     * Used in query: "find users by province code OR province name."
     */
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    public User() {
    }

    public User(String username, String email, String password, EUserRole role,
                UserProfile userProfile, Location location) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.userProfile = userProfile;
        this.location = location;
    }

    // ── Getters & Setters ──────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public EUserRole getRole() { return role; }
    public void setRole(EUserRole role) { this.role = role; }

    public UserProfile getUserProfile() { return userProfile; }
    public void setUserProfile(UserProfile userProfile) { this.userProfile = userProfile; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
}
