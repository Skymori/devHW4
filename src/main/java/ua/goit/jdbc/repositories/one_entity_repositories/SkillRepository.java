package ua.goit.jdbc.repositories.one_entity_repositories;


import ua.goit.jdbc.config.DatabaseManagerConnection;
import ua.goit.jdbc.dao.SkillDAO;
import ua.goit.jdbc.converters.SkillConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SkillRepository implements Repository<SkillDAO> {
    private final DatabaseManagerConnection connectionManager;
    private static final String deleteById = "DELETE FROM skills WHERE skill_id=?";
    private static final String update = "UPDATE skills SET language =?::language_choice, LEVEL=?::level_choice WHERE skill_id=?";
    private static final String create = "INSERT INTO skills (language, level) VALUES (?::language_choice,?::level_choice)";
    private static final String findById = "SELECT skill_id, LANGUAGE , LEVEL FROM skills WHERE skill_id=?";
    private static final String findAll = "SELECT skill_id, LANGUAGE, LEVEL FROM skills";
    private static final String getListOfValidIndexes = "SELECT skill_id FROM skills ORDER BY skill_id";
    private static final String getLastSkillIndex = "SELECT skill_id FROM skills ORDER BY skill_id DESC limit 1";

    public SkillRepository(DatabaseManagerConnection connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public SkillDAO deleteById(int skillId) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteById)) {
            preparedStatement.setInt(1, skillId);
            SkillDAO deletedSkillDAO = findById(skillId);
            preparedStatement.executeUpdate();
            return deletedSkillDAO;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public SkillDAO deleteByObject(SkillDAO skillDAO) {
        return deleteById(skillDAO.getSkillId());
    }

    @Override
    public SkillDAO update(SkillDAO skillDAO) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(update)) {
            preparedStatement.setInt(3, skillDAO.getSkillId());
            setValuesAndExecutePreparedStatement(skillDAO, preparedStatement);
            return findById(skillDAO.getSkillId());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public SkillDAO create(SkillDAO skillDAO) {
        if (!exists(skillDAO)) {
            try (Connection connection = connectionManager.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(create)) {
                setValuesAndExecutePreparedStatement(skillDAO, preparedStatement);
                skillDAO.setSkillId(getLastSkillIndex());
                return skillDAO;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            skillDAO.setSkillId(getIdForExistingSkill(skillDAO));
            return skillDAO;
        }
    }

    @Override
    public SkillDAO findById(int skillId) {
        SkillDAO skillDAO = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findById)) {
            preparedStatement.setInt(1, skillId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                skillDAO = SkillConverter.toSkillDAO(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return skillDAO;
    }

    @Override
    public List<SkillDAO> findAll() {
        List<SkillDAO> allSkillDAOS = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findAll)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                allSkillDAOS.add(SkillConverter.toSkillDAO(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allSkillDAOS;
    }

    @Override
    public List<Integer> getListOfValidIndexes() {
        List<Integer> indexes = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getListOfValidIndexes)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                indexes.add(resultSet.getInt("skill_id"));
            }
            return indexes;
        } catch (SQLException e) {
            e.printStackTrace();
            return indexes;
        }
    }

    private boolean exists(SkillDAO skillDAO) {
        return findAll().stream()
                .anyMatch(s -> s.equals(skillDAO));
    }

    private int getIdForExistingSkill(SkillDAO skillDAO) {
        return findAll().stream()
                .filter(s -> s.equals(skillDAO))
                .findFirst().orElse(new SkillDAO()).getSkillId();
    }

    private void setValuesAndExecutePreparedStatement(SkillDAO skillDAO, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, skillDAO.getLanguage());
        preparedStatement.setString(2, skillDAO.getLevel());
        preparedStatement.executeUpdate();
    }

    private int getLastSkillIndex() {
        int index = -1;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getLastSkillIndex)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                index = resultSet.getInt("skill_id");
            }
            return index;
        } catch (SQLException e) {
            e.printStackTrace();
            return index;
        }
    }
}
