package ua.goit.jdbc.repositories.one_entity_repositories;



import ua.goit.jdbc.config.DatabaseManagerConnection;
import ua.goit.jdbc.dao.ProjectDAO;
import ua.goit.jdbc.converters.ProjectConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectRepository implements Repository<ProjectDAO> {
    private final DatabaseManagerConnection connectionManager;
    private static final String deleteById = "DELETE FROM projects WHERE project_id=?";
    private static final String update = "UPDATE projects SET name=?, description=?, cost=?, creation_date=? WHERE project_id=?";
    private static final String create = "INSERT INTO projects (name, description, cost, creation_date) VALUES (?,?,?,?)";
    private static final String findById = "SELECT project_id, name, description, cost, creation_date FROM projects WHERE project_id=?";
    private static final String findAll = "SELECT project_id, name, description, cost, creation_date FROM projects";
    private static final String getListOfValidIndexes = "select project_id from projects order by project_id";
    private static final String getLastProjectIndex = "SELECT project_id FROM projects ORDER BY project_id DESC limit 1";



    public ProjectRepository(DatabaseManagerConnection connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public ProjectDAO deleteById(int projectId) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteById)) {
            preparedStatement.setInt(1, projectId);
            ProjectDAO deletedProjectDAO = findById(projectId);
            preparedStatement.executeUpdate();
            return deletedProjectDAO;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProjectDAO deleteByObject(ProjectDAO projectDAO) {
        return deleteById(projectDAO.getProjectId());
    }

    @Override
    public ProjectDAO update(ProjectDAO locationDAO) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(update)) {
            preparedStatement.setInt(5, locationDAO.getProjectId());
            setValuesAndExecutePreparedStatement(locationDAO, preparedStatement);
            return findById(locationDAO.getProjectId());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProjectDAO create(ProjectDAO projectDAO) {
        if (!exists(projectDAO)) {
            try (Connection connection = connectionManager.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(create)) {
                setValuesAndExecutePreparedStatement(projectDAO, preparedStatement);
                projectDAO.setProjectId(getLastProjectIndex());
                return projectDAO;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            projectDAO.setProjectId(getIdForExistingProject(projectDAO));
            return projectDAO;
        }
    }

    @Override
    public ProjectDAO findById(int projectId) {
        ProjectDAO projectDAO = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findById)) {
            preparedStatement.setInt(1, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                projectDAO = ProjectConverter.toProjectDAO(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projectDAO;
    }

    @Override
    public List<ProjectDAO> findAll() {
        List<ProjectDAO> projectDAOS = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findAll)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                projectDAOS.add(ProjectConverter.toProjectDAO(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projectDAOS;
    }

    @Override
    public List<Integer> getListOfValidIndexes() {
        List<Integer> indexes = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getListOfValidIndexes)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                indexes.add(resultSet.getInt("project_id"));
            }
            return indexes;
        } catch (SQLException e) {
            e.printStackTrace();
            return indexes;
        }
    }

    private boolean exists(ProjectDAO projectDAO) {
        return findAll().stream()
                .anyMatch(p -> p.equals(projectDAO));
    }

    private int getIdForExistingProject(ProjectDAO projectDAO) {
        return findAll().stream()
                .filter(p -> p.equals(projectDAO))
                .findFirst().orElse(new ProjectDAO()).getProjectId();
    }

    private void setValuesAndExecutePreparedStatement(ProjectDAO projectDAO, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, projectDAO.getName());
        preparedStatement.setString(2, projectDAO.getDescription());
        preparedStatement.setDouble(3, projectDAO.getCost());
        preparedStatement.setDate(4, java.sql.Date.valueOf(projectDAO.getDate()));
        preparedStatement.executeUpdate();
    }

    private int getLastProjectIndex() {
        int index = -1;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getLastProjectIndex)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                index = resultSet.getInt("project_id");
            }
            return index;
        } catch (SQLException e) {
            e.printStackTrace();
            return index;
        }
    }
}
