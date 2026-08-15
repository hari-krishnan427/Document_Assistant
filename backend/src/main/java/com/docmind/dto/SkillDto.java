package com.docmind.dto;

public class SkillDto {
    private Long id;
    private String skillName;
    private String proficiencyLevel;
    private Long verifiedByDocId;
    private String verifiedByDocName;

    public SkillDto() {}

    public SkillDto(Long id, String skillName, String proficiencyLevel, Long verifiedByDocId, String verifiedByDocName) {
        this.id = id;
        this.skillName = skillName;
        this.proficiencyLevel = proficiencyLevel;
        this.verifiedByDocId = verifiedByDocId;
        this.verifiedByDocName = verifiedByDocName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public String getProficiencyLevel() { return proficiencyLevel; }
    public void setProficiencyLevel(String proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; }

    public Long getVerifiedByDocId() { return verifiedByDocId; }
    public void setVerifiedByDocId(Long verifiedByDocId) { this.verifiedByDocId = verifiedByDocId; }

    public String getVerifiedByDocName() { return verifiedByDocName; }
    public void setVerifiedByDocName(String verifiedByDocName) { this.verifiedByDocName = verifiedByDocName; }

    public static SkillDtoBuilder builder() { return new SkillDtoBuilder(); }

    public static class SkillDtoBuilder {
        private Long id;
        private String skillName;
        private String proficiencyLevel;
        private Long verifiedByDocId;
        private String verifiedByDocName;

        public SkillDtoBuilder id(Long id) { this.id = id; return this; }
        public SkillDtoBuilder skillName(String skillName) { this.skillName = skillName; return this; }
        public SkillDtoBuilder proficiencyLevel(String proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; return this; }
        public SkillDtoBuilder verifiedByDocId(Long verifiedByDocId) { this.verifiedByDocId = verifiedByDocId; return this; }
        public SkillDtoBuilder verifiedByDocName(String verifiedByDocName) { this.verifiedByDocName = verifiedByDocName; return this; }

        public SkillDto build() {
            return new SkillDto(id, skillName, proficiencyLevel, verifiedByDocId, verifiedByDocName);
        }
    }
}
