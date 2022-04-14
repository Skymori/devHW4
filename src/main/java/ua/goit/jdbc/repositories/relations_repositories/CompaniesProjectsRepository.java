package ua.goit.jdbc.repositories.relations_repositories;



import ua.goit.jdbc.config.DatabaseManagerConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompaniesProjectsRepository implements RelationsRepository {
    private final DatabaseManagerConnection connectionManager;
    private static final String delete = "DELETE FROM companies_projects cp WHERE cp.company_id=? AND cp.project_id=?";
    private static final String create = "INSERT INTO companies_projects(company_id, project_id) VALUES (?,?)";
    private static final String exists = "SELECT company_id, project_id FROM companies_projects WHERE company_id=? AND project_id=?";
    public CompaniesProjectsRepository(DatabaseManagerConnection connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public boolean delete(int companyId, int projectId) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(delete)) {
            preparedStatement.setInt(1, companyId);
            preparedStatement.setInt(2, projectId);
            int rows = preparedStatement.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean create(int companyId, int projectId) {
        if (!exists(companyId, projectId)) {
            try (Connection connection = connectionManager.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(create)) {
                preparedStatement.setInt(1, companyId);
                preparedStatement.setInt(2, projectId);
                int rows = preparedStatement.executeUpdate();
                return rows == 1;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean exists(int companyId, int projectId) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(exists)) {
            preparedStatement.setInt(1, companyId);
            preparedStatement.setInt(2, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
