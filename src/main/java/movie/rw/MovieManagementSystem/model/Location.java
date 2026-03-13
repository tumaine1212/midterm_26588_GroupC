package movie.rw.MovieManagementSystem.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Represents a geographical location in a hierarchy: Province → District → Sector.
 *
 * SELF-REFERENCING Many-to-One relationship:
 *   A Province has no parent (parent = null).
 *   A District's parent is a Province.
 *   A Sector's parent is a District.
 *
 * This single table holds all location levels using the 'type' field
 * (ELocationType) to distinguish them, and the 'parent_id' foreign key
 * to express the hierarchy.
 */
@Entity
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Unique code identifying this location (e.g., "KG" for Kigali, "NG" for Ngoma).
     */
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    /**
     * Enum-mapped column stored as a STRING in the database.
     * Tells us whether this row is a PROVINCE, DISTRICT, or SECTOR.
     */
    @Column(name = "location_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ELocationType type;

    /**
     * Many-to-One self-reference:
     *   Many districts share one province as parent.
     *   Many sectors share one district as parent.
     * The 'parent_id' column in the same table holds the FK.
     */
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Location parent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Location() {
    }

    public Location(Long id, String name, String code, ELocationType type, Location parent, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.type = type;
        this.parent = parent;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * @PrePersist lifecycle hook: automatically sets createdAt
     * to the current time just before the entity is first saved.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // ── Getters & Setters ──────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public ELocationType getType() { return type; }
    public void setType(ELocationType type) { this.type = type; }

    public Location getParent() { return parent; }
    public void setParent(Location parent) { this.parent = parent; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
