package ua.goit.jdbc.dto;


import ua.goit.jdbc.dao.CompanyDAO;
import ua.goit.jdbc.dao.CustomerDAO;
import ua.goit.jdbc.dao.DeveloperDAO;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class ProjectDTO {
    private int projectId;
    private String name;
    private String description;
    private double cost;
    private LocalDate date;
    private List<DeveloperDAO> developers;
    private List<CompanyDAO> companies;
    private List<CustomerDAO> customers;

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public List<DeveloperDAO> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<DeveloperDAO> developers) {
        this.developers = developers;
    }

    public List<CompanyDAO> getCompanies() {
        return companies;
    }

    public void setCompanies(List<CompanyDAO> companies) {
        this.companies = companies;
    }

    public List<CustomerDAO> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerDAO> customers) {
        this.customers = customers;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ProjectDTO{" +
                "projectId=" + projectId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cost=" + cost +
                ", date=" + date +
                ", developers=" + developers +
                ", companies=" + companies +
                ", customers=" + customers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectDTO projectTo = (ProjectDTO) o;
        return Double.compare(projectTo.cost, cost) == 0 && Objects.equals(name, projectTo.name) && Objects.equals(description, projectTo.description) && Objects.equals(date, projectTo.date) && Objects.equals(developers, projectTo.developers) && Objects.equals(companies, projectTo.companies) && Objects.equals(customers, projectTo.customers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, cost, date, developers, companies, customers);
    }
}
