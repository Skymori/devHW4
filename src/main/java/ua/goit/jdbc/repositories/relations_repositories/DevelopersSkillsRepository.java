package ua.goit.jdbc.repositories.relations_repositories;

import ua.goit.jdbc.config.DatabaseManagerConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DevelopersSkillsRepository implements RelationsRepository {
    private final DatabaseManagerConnection connectionManager;
    private static final String delete = "DELETE FROM developers_skills WHERE developer_id=? AND skill_id=?";
    private static final String create = "INSERT INTO developers_skills(developer_id, skill_id) VALUES (?,?)";
    private static final String exists = "SELECT developer_id, skill_id FROM developers_skills WHERE developer_id=? AND skill_id=?";

    public DevelopersSkillsRepository(DatabaseManagerConnection connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public boolean delete(int developerId, int skillId) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(delete)) {
            preparedStatement.setInt(1, developerId);
            preparedStatement.setInt(2, skillId);
            int rows = preparedStatement.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean create(int developerId, int skillId) {
        if (!exists(developerId, skillId)) {
            try (Connection connection = connectionManager.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(create)) {
                preparedStatement.setInt(1, developerId);
                preparedStatement.setInt(2, skillId);
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
    public boolean exists(int developerId, int skillId) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(exists)) {
            preparedStatement.setInt(1, developerId);
            preparedStatement.setInt(2, skillId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
