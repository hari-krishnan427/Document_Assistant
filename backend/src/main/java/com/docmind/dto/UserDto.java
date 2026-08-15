package com.docmind.dto;

import java.time.LocalDateTime;

public class UserDto {
    private Long id;
    private String email;
    private String fullName;
    private String role;
    private LocalDateTime createdAt;

    public UserDto() {}

    public UserDto(Long id, String email, String fullName, String role, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static UserDtoBuilder builder() { return new UserDtoBuilder(); }

    public static class UserDtoBuilder {
        private Long id;
        private String email;
        private String fullName;
        private String role;
        private LocalDateTime createdAt;

        public UserDtoBuilder id(Long id) { this.id = id; return this; }
        public UserDtoBuilder email(String email) { this.email = email; return this; }
        public UserDtoBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public UserDtoBuilder role(String role) { this.role = role; return this; }
        public UserDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public UserDto build() {
            return new UserDto(id, email, fullName, role, createdAt);
        }
    }
}
