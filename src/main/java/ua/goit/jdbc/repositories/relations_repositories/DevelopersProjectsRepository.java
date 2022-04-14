package ua.goit.jdbc.repositories.relations_repositories;

import ua.goit.jdbc.config.DatabaseManagerConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DevelopersProjectsRepository implements RelationsRepository {
    private final DatabaseManagerConnection connectionManager;
    private static final String delete = "DELETE FROM developers_projects WHERE developer_id=? AND project_id=?";
    private static final String create = "INSERT INTO developers_projects(developer_id, project_id) VALUES (?,?)";
    private static final String exists = "SELECT developer_id, project_id FROM developers_projects WHERE developer_id=? AND project_id=?";

    public DevelopersProjectsRepository(DatabaseManagerConnection connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public boolean delete(int developerId, int projectId) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(delete)) {
            preparedStatement.setInt(1, developerId);
            preparedStatement.setInt(2, projectId);
            int rows = preparedStatement.executeUpdate();
            if (rows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean create(int developerId, int projectId) {
        if (!exists(developerId, projectId)) {
            try (Connection connection = connectionManager.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(create)) {
                preparedStatement.setInt(1, developerId);
                preparedStatement.setInt(2, projectId);
                int rows = preparedStatement.executeUpdate();
                if (rows == 1) {
                    return true;
                } else {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean exists(int developerId, int projectId) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(exists)) {
            preparedStatement.setInt(1, developerId);
            preparedStatement.setInt(2, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
