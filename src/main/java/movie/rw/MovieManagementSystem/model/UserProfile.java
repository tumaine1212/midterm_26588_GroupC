package movie.rw.MovieManagementSystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Represents extended profile information for a User.
 *
 * ONE-TO-ONE relationship with User:
 *   One User has exactly ONE UserProfile, and vice versa.
 *   The FK 'user_id' lives on the User table (User owns the relationship).
 *   This entity is the INVERSE side, so 'mappedBy = "userProfile"'
 *   points to the field name in User.java.
 */
@Entity
@Table(name = "user_profile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bio")
    private String bio;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * Inverse side of the One-to-One.
     * 'mappedBy' matches the field in User.java that owns the FK.
     * @JsonIgnore prevents infinite recursion during JSON serialization.
     */
    @OneToOne(mappedBy = "userProfile")
    @JsonIgnore
    private User user;

    public UserProfile() {
    }

    public UserProfile(String bio, String avatarUrl, String phoneNumber) {
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        this.phoneNumber = phoneNumber;
    }

    // ── Getters & Setters ──────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
