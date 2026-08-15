package com.docmind.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserProfileDto {
    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String location;
    private String bio;
    private Integer readinessScore;
    
    private List<SkillDto> skills = new ArrayList<>();
    private List<EducationDto> education = new ArrayList<>();
    private List<ExperienceDto> experience = new ArrayList<>();
    private List<CertificationDto> certifications = new ArrayList<>();

    public UserProfileDto() {}

    public UserProfileDto(Long id, Long userId, String fullName, String email, String phoneNumber, 
                          LocalDate dateOfBirth, String gender, String location, String bio, 
                          Integer readinessScore, List<SkillDto> skills, List<EducationDto> education, 
                          List<ExperienceDto> experience, List<CertificationDto> certifications) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.location = location;
        this.bio = bio;
        this.readinessScore = readinessScore != null ? readinessScore : 0;
        this.skills = skills != null ? skills : new ArrayList<>();
        this.education = education != null ? education : new ArrayList<>();
        this.experience = experience != null ? experience : new ArrayList<>();
        this.certifications = certifications != null ? certifications : new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

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

    public List<SkillDto> getSkills() { return skills; }
    public void setSkills(List<SkillDto> skills) { this.skills = skills; }

    public List<EducationDto> getEducation() { return education; }
    public void setEducation(List<EducationDto> education) { this.education = education; }

    public List<ExperienceDto> getExperience() { return experience; }
    public void setExperience(List<ExperienceDto> experience) { this.experience = experience; }

    public List<CertificationDto> getCertifications() { return certifications; }
    public void setCertifications(List<CertificationDto> certifications) { this.certifications = certifications; }

    public static UserProfileDtoBuilder builder() { return new UserProfileDtoBuilder(); }

    public static class UserProfileDtoBuilder {
        private Long id;
        private Long userId;
        private String fullName;
        private String email;
        private String phoneNumber;
        private LocalDate dateOfBirth;
        private String gender;
        private String location;
        private String bio;
        private Integer readinessScore = 0;
        private List<SkillDto> skills = new ArrayList<>();
        private List<EducationDto> education = new ArrayList<>();
        private List<ExperienceDto> experience = new ArrayList<>();
        private List<CertificationDto> certifications = new ArrayList<>();

        public UserProfileDtoBuilder id(Long id) { this.id = id; return this; }
        public UserProfileDtoBuilder userId(Long userId) { this.userId = userId; return this; }
        public UserProfileDtoBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public UserProfileDtoBuilder email(String email) { this.email = email; return this; }
        public UserProfileDtoBuilder phoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }
        public UserProfileDtoBuilder dateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; return this; }
        public UserProfileDtoBuilder gender(String gender) { this.gender = gender; return this; }
        public UserProfileDtoBuilder location(String location) { this.location = location; return this; }
        public UserProfileDtoBuilder bio(String bio) { this.bio = bio; return this; }
        public UserProfileDtoBuilder readinessScore(Integer readinessScore) { this.readinessScore = readinessScore; return this; }
        public UserProfileDtoBuilder skills(List<SkillDto> skills) { this.skills = skills; return this; }
        public UserProfileDtoBuilder education(List<EducationDto> education) { this.education = education; return this; }
        public UserProfileDtoBuilder experience(List<ExperienceDto> experience) { this.experience = experience; return this; }
        public UserProfileDtoBuilder certifications(List<CertificationDto> certifications) { this.certifications = certifications; return this; }

        public UserProfileDto build() {
            return new UserProfileDto(id, userId, fullName, email, phoneNumber, dateOfBirth, gender, location, bio, readinessScore, skills, education, experience, certifications);
        }
    }
}
