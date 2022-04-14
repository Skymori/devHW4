package ua.goit.jdbc.repositories.one_entity_repositories;



import ua.goit.jdbc.config.DatabaseManagerConnection;
import ua.goit.jdbc.dao.CompanyDAO;
import ua.goit.jdbc.converters.CompanyConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyRepository implements Repository<CompanyDAO> {
    private final DatabaseManagerConnection connectionManager;
    private static final String DELETE_BY_ID = "DELETE FROM companies WHERE company_id=?";
    private static final String UPDATE = "UPDATE companies SET name=?, city=? WHERE company_id=?";
    private static final String CREATE = "INSERT INTO companies (name, city) VALUES (?,?)";
    private static final String FIND_BY_ID = "SELECT company_id, name, city FROM companies WHERE company_id=?";
    private static final String FIND_ALL = "SELECT company_id, name, city FROM companies";
    private static final String GET_LIST_OF_VALID_INDEXES = "SELECT company_id FROM companies ORDER BY company_id";
    private static final String GET_LAST_COMPANY_INDEX = "SELECT company_id FROM companies ORDER BY company_id DESC limit 1";

    public CompanyRepository(DatabaseManagerConnection connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public CompanyDAO deleteById(int companyId) {

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {
            preparedStatement.setInt(1, companyId);
            CompanyDAO deletedCompanyDao = findById(companyId);
            preparedStatement.executeUpdate();
            return deletedCompanyDao;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public CompanyDAO deleteByObject(CompanyDAO companyDAO) {
        return deleteById(companyDAO.getCompanyId());
    }

    @Override
    public CompanyDAO update(CompanyDAO companyDao) {

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setInt(3, companyDao.getCompanyId());
            setValuesAndExecutePreparedStatement(companyDao, preparedStatement);
            return findById(companyDao.getCompanyId());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public CompanyDAO create(CompanyDAO companyDAO) {
        if (!exists(companyDAO)) {

            try (Connection connection = connectionManager.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(CREATE)) {
                setValuesAndExecutePreparedStatement(companyDAO, preparedStatement);
                companyDAO.setCompanyId(getLastCompanyIndex());
                return companyDAO;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            companyDAO.setCompanyId(getIdForExistingCompany(companyDAO));
            return companyDAO;
        }
    }

    @Override
    public CompanyDAO findById(int companyId) {

        CompanyDAO companyDAO = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setInt(1, companyId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                companyDAO = CompanyConverter.toCompanyDAO(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companyDAO;
    }

    @Override
    public List<CompanyDAO> findAll() {

        List<CompanyDAO> allCompanies = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                allCompanies.add(CompanyConverter.toCompanyDAO(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCompanies;
    }

    @Override
    public List<Integer> getListOfValidIndexes() {

        List<Integer> indexes = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_LIST_OF_VALID_INDEXES)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                indexes.add(resultSet.getInt("id_company"));
            }
            return indexes;
        } catch (SQLException e) {
            e.printStackTrace();
            return indexes;
        }
    }

    private boolean exists(CompanyDAO companyDAO) {
        return findAll().stream()
                .anyMatch(c -> c.equals(companyDAO));
    }

    private int getIdForExistingCompany(CompanyDAO companyDAO) {
        return findAll().stream()
                .filter(c -> c.equals(companyDAO))
                .findFirst().orElse(new CompanyDAO()).getCompanyId();
    }


    private void setValuesAndExecutePreparedStatement(CompanyDAO companyDAO, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, companyDAO.getName());
        preparedStatement.setString(2, companyDAO.getCity());
        preparedStatement.executeUpdate();
    }

    private int getLastCompanyIndex() {

        int index = -1;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_LAST_COMPANY_INDEX)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                index = resultSet.getInt("company_id");
            }
            return index;
        } catch (SQLException e) {
            e.printStackTrace();
            return index;
        }
    }
}
