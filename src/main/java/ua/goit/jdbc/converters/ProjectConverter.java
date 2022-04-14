package ua.goit.jdbc.converters;

import ua.goit.jdbc.dao.ProjectDAO;
import ua.goit.jdbc.dto.ProjectDTO;
import ua.goit.jdbc.util.RelationsUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectConverter {
    public static ProjectDAO toProjectDAO(ProjectDTO projectDTO) {
        ProjectDAO projectDAO = new ProjectDAO();
        projectDAO.setProjectId(projectDTO.getProjectId());
        projectDAO.setName(projectDTO.getName());
        projectDAO.setDescription(projectDTO.getDescription());
        projectDAO.setCost(projectDTO.getCost());
        projectDAO.setDate(projectDTO.getDate());
        return projectDAO;
    }

    public static ProjectDTO fromProjectDAO(ProjectDAO projectDAO) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectId(projectDAO.getProjectId());
        projectDTO.setName(projectDAO.getName());
        projectDTO.setDescription(projectDAO.getDescription());
        projectDTO.setCost(projectDAO.getCost());
        projectDTO.setDate(projectDAO.getDate());
        projectDTO.setCompanies(RelationsUtils.getAllCompaniesForProject(projectDAO.getProjectId()));
        projectDTO.setCustomers(RelationsUtils.getCustomerForProject(projectDAO.getProjectId()));
        projectDTO.setDevelopers(RelationsUtils.getAllDevelopersForProject(projectDAO.getProjectId()));
        return projectDTO;
    }

    public static ProjectDAO toProjectDAO(ResultSet resultSet) throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        projectDAO.setProjectId(resultSet.getInt("project_id"));
        projectDAO.setName(resultSet.getString("name"));
        projectDAO.setDescription(resultSet.getString("description"));
        projectDAO.setCost(resultSet.getDouble("cost"));
        projectDAO.setDate(resultSet.getObject("creation_date", LocalDate.class));
        return projectDAO;
    }

    public static List<ProjectDTO> allFromProjectDAO(List<ProjectDAO> projectDAOS) {
        return projectDAOS.stream()
                .map(ProjectConverter::fromProjectDAO)
                .collect(Collectors.toList());
    }
}
