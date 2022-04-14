package ua.goit.jdbc.dto;


import ua.goit.jdbc.dao.CompanyDAO;
import ua.goit.jdbc.dao.ProjectDAO;
import ua.goit.jdbc.dao.SkillDAO;

import java.util.List;
import java.util.Objects;

public class DeveloperDTO {
    private int developerId;
    private String name;
    private int age;
    private String sex;
    private CompanyDAO companyDAO;
    private double salary;
    private List<SkillDAO> skills;
    private List<ProjectDAO> projects;

    public int getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(int developerId) {
        this.developerId = developerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public CompanyDAO getCompanyDAO() {
        return companyDAO;
    }

    public void setCompanyDAO(CompanyDAO companyDAO) {
        this.companyDAO = companyDAO;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public List<SkillDAO> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillDAO> skills) {
        this.skills = skills;
    }

    public List<ProjectDAO> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectDAO> projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return "DeveloperTo{" +
                "idDeveloper=" + developerId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", companyDao=" + companyDAO +
                ", salary=" + salary +
                ", skills=" + skills +
                ", projects=" + projects +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeveloperDTO that = (DeveloperDTO) o;
        return age == that.age && Double.compare(that.salary, salary) == 0 && Objects.equals(name, that.name) && Objects.equals(sex, that.sex) && Objects.equals(companyDAO, that.companyDAO) && Objects.equals(skills, that.skills) && Objects.equals(projects, that.projects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, sex, companyDAO, salary, skills, projects);
    }
}
