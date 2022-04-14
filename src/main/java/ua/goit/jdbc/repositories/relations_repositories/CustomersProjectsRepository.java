package ua.goit.jdbc.repositories.relations_repositories;

import ua.goit.jdbc.config.DatabaseManagerConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomersProjectsRepository implements RelationsRepository {
    private final DatabaseManagerConnection connectionManager;
    private static final String delete = "DELETE FROM customers_projects cp WHERE cp.customer_id=? AND cp.project_id=?";
    private static final String create = "INSERT INTO customers_projects(customer_id, project_id) VALUES (?,?)";
    private static final String exists = "SELECT customer_id, project_id FROM customers_projects where customer_id=? AND project_id=?";

    public CustomersProjectsRepository(DatabaseManagerConnection connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public boolean delete(int customerId, int projectId) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(delete)) {
            preparedStatement.setInt(1, customerId);
            preparedStatement.setInt(2, projectId);
            int rows = preparedStatement.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean create(int customerId, int projectId) {
        if (!exists(customerId, projectId)) {
            try (Connection connection = connectionManager.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(create)) {
                preparedStatement.setInt(1, customerId);
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
    public boolean exists(int customerId, int projectId) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(exists)) {
            preparedStatement.setInt(1, customerId);
            preparedStatement.setInt(2, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
