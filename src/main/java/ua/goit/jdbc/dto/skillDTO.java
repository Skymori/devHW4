package ua.goit.jdbc.dto;

import java.util.Objects;

public class skillDTO {
    private int skillId;
    private String language;
    private String level;

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "SkillDTO{" +
                "skillId=" + skillId +
                ", language='" + language + '\'' +
                ", level='" + level + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        skillDTO skillTo = (skillDTO) o;
        return Objects.equals(language, skillTo.language) && Objects.equals(level, skillTo.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(language, level);
    }
}
