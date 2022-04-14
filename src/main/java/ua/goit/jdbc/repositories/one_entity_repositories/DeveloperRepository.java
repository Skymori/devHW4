package ua.goit.jdbc.repositories.one_entity_repositories;


import ua.goit.jdbc.config.DatabaseManagerConnection;
import ua.goit.jdbc.dao.DeveloperDAO;
import ua.goit.jdbc.converters.DeveloperConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeveloperRepository implements Repository<DeveloperDAO> {
    private final DatabaseManagerConnection connectionManager;
    private static final String getLastDeveloperIndex = "SELECT developer_id FROM developers ORDER BY developer_id DESC limit 1";
    private static final String getListOfValidIndexes = "SELECT developer_id FROM developers ORDER BY developer_id";
    private static final String findAll = "SELECT developer_id, name, age, sex, company_id, salary FROM developers";
    private static final String findById = "SELECT developer_id, name, age, sex, company_id, salary FROM developers WHERE developer_id=?";
    private static final String create = "INSERT INTO developers (name, age, sex, company_id, salary) VALUES (?,?,?::sex_choice,?,?)";
    private static final String update = "UPDATE developers SET name=?, age=?, sex=?::sex_choice, company_id=?, salary=? WHERE  developer_id=?";
    private static final String deleteById = "DELETE FROM developers WHERE developer_id=?";





    public DeveloperRepository(DatabaseManagerConnection connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public DeveloperDAO deleteById(int developerId) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteById)) {
            preparedStatement.setInt(1, developerId);
            DeveloperDAO deleteDeveloperDAO = findById(developerId);
            preparedStatement.executeUpdate();
            return deleteDeveloperDAO;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public DeveloperDAO deleteByObject(DeveloperDAO developerDAO) {
        return deleteById(developerDAO.getDeveloperId());
    }

    @Override
    public DeveloperDAO update(DeveloperDAO developerDAO) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(update)) {
            preparedStatement.setInt(6, developerDAO.getDeveloperId());
            setValuesAndExecutePreparedStatement(developerDAO, preparedStatement);
            return findById(developerDAO.getDeveloperId());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public DeveloperDAO create(DeveloperDAO developerDAO) {
        if (!exists(developerDAO)) {
            try (Connection connection = connectionManager.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(create)) {
                setValuesAndExecutePreparedStatement(developerDAO, preparedStatement);
                developerDAO.setDeveloperId(getLastDeveloperIndex());
                return developerDAO;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            developerDAO.setDeveloperId(getIdForExistingDeveloper(developerDAO));
            return developerDAO;
        }
    }

    @Override
    public DeveloperDAO findById(int developerId) {
        DeveloperDAO developerDAO = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findById)) {
            preparedStatement.setInt(1, developerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                developerDAO = DeveloperConverter.toDeveloperDAO(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return developerDAO;
    }

    @Override
    public List<DeveloperDAO> findAll() {
        List<DeveloperDAO> allDeveloperDAOS = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findAll)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                allDeveloperDAOS.add(DeveloperConverter.toDeveloperDAO(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allDeveloperDAOS;
    }

    @Override
    public List<Integer> getListOfValidIndexes() {
        List<Integer> indexes = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getListOfValidIndexes)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                indexes.add(resultSet.getInt("developer_id"));
            }
            return indexes;
        } catch (SQLException e) {
            e.printStackTrace();
            return indexes;
        }
    }

    private boolean exists(DeveloperDAO developerDAO) {
        return findAll().stream()
                .anyMatch(d -> d.equals(developerDAO));
    }

    private int getIdForExistingDeveloper(DeveloperDAO developerDAO) {
        return findAll().stream()
                .filter(d -> d.equals(developerDAO))
                .findFirst().orElse(new DeveloperDAO()).getDeveloperId();
    }

    private void setValuesAndExecutePreparedStatement(DeveloperDAO developerDAO, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, developerDAO.getName());
        preparedStatement.setInt(2, developerDAO.getAge());
        preparedStatement.setString(3, developerDAO.getSex());
        preparedStatement.setInt(4, developerDAO.getCompanyId());
        preparedStatement.setDouble(5, developerDAO.getSalary());
        preparedStatement.executeUpdate();
    }

    private int getLastDeveloperIndex() {
        int index = -1;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getLastDeveloperIndex)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                index = resultSet.getInt("developer_id");
            }
            return index;
        } catch (SQLException e) {
            e.printStackTrace();
            return index;
        }
    }
}
