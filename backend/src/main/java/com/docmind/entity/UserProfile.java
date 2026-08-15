package com.docmind.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String gender;
    private String location;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "readiness_score")
    private Integer readinessScore = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UserProfile() {}

    public UserProfile(Long id, User user, String phoneNumber, LocalDate dateOfBirth, String gender, String location, String bio, Integer readinessScore, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.location = location;
        this.bio = bio;
        this.readinessScore = readinessScore != null ? readinessScore : 0;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public Integer getReadinessScore() { return readinessScore; }
    public void setReadinessScore(Integer readinessScore) { this.readinessScore = readinessScore; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static UserProfileBuilder builder() { return new UserProfileBuilder(); }

    public static class UserProfileBuilder {
        private Long id;
        private User user;
        private String phoneNumber;
        private LocalDate dateOfBirth;
        private String gender;
        private String location;
        private String bio;
        private Integer readinessScore = 0;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public UserProfileBuilder id(Long id) { this.id = id; return this; }
        public UserProfileBuilder user(User user) { this.user = user; return this; }
        public UserProfileBuilder phoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }
        public UserProfileBuilder dateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; return this; }
        public UserProfileBuilder gender(String gender) { this.gender = gender; return this; }
        public UserProfileBuilder location(String location) { this.location = location; return this; }
        public UserProfileBuilder bio(String bio) { this.bio = bio; return this; }
        public UserProfileBuilder readinessScore(Integer readinessScore) { this.readinessScore = readinessScore; return this; }
        public UserProfileBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public UserProfileBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public UserProfile build() {
            return new UserProfile(id, user, phoneNumber, dateOfBirth, gender, location, bio, readinessScore, createdAt, updatedAt);
        }
    }
}
