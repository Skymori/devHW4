package ua.goit.jdbc.dto;


import ua.goit.jdbc.dao.ProjectDAO;

import java.util.List;
import java.util.Objects;

public class CustomerDTO {
    private int customerId;
    private String name;
    private String city;
    private List<ProjectDAO> projects;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<ProjectDAO> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectDAO> projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", projects=" + projects +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerDTO that = (CustomerDTO) o;
        return Objects.equals(name, that.name) && Objects.equals(city, that.city) && Objects.equals(projects, that.projects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, city, projects);
    }
}
