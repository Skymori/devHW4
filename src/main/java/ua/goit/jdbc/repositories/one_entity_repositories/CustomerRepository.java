package ua.goit.jdbc.repositories.one_entity_repositories;



import ua.goit.jdbc.config.DatabaseManagerConnection;
import ua.goit.jdbc.dao.CustomerDAO;
import ua.goit.jdbc.converters.CustomerConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository implements Repository<CustomerDAO> {
    private final DatabaseManagerConnection connectionManager;
    private static final String DELETE_BY_ID = "DELETE FROM customers WHERE customer_id=?";
    private static final String UPDATE = "UPDATE customers SET name=?, city=? WHERE customer_id=?";
    private static final String CREATE = "INSERT INTO customers (name, city) VALUES (?,?)";
    private static final String FIND_BY_ID = "SELECT customer_id, name, city FROM customers WHERE customer_id=?";
    private static final String FIND_ALL = "SELECT customer_id, name, city FROM customers";
    private static final String GET_LIST_OF_VALID_INDEXES = "SELECT customer_id, name, city FROM customers";
    private static final String GET_LAST_CUSTOMER_INDEX = "SELECT customer_id FROM customers ORDER BY customer_id DESC limit 1";

    public CustomerRepository(DatabaseManagerConnection connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public CustomerDAO deleteById(int customerId) {

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {
            preparedStatement.setInt(1, customerId);
            CustomerDAO deletedCustomerDAO = findById(customerId);
            preparedStatement.executeUpdate();
            return deletedCustomerDAO;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public CustomerDAO deleteByObject(CustomerDAO customerDAO) {
        return deleteById(customerDAO.getCustomerId());
    }

    @Override
    public CustomerDAO update(CustomerDAO customerDAO) {

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setInt(3, customerDAO.getCustomerId());
            setValuesAndExecutePreparedStatement(customerDAO, preparedStatement);
            return findById(customerDAO.getCustomerId());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public CustomerDAO create(CustomerDAO customerDAO) {
        if (!exists(customerDAO)) {

            try (Connection connection = connectionManager.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(CREATE)) {
                setValuesAndExecutePreparedStatement(customerDAO, preparedStatement);
                customerDAO.setCustomerId(getLastCustomerIndex());
                return customerDAO;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            customerDAO.setCustomerId(getIdForExistingCustomer(customerDAO));
            return customerDAO;
        }
    }

    @Override
    public CustomerDAO findById(int customerId) {

        CustomerDAO customerDAO = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                customerDAO = CustomerConverter.toCustomerDAO(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerDAO;
    }

    @Override
    public List<CustomerDAO> findAll() {

        List<CustomerDAO> allCustomerDAOS = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                allCustomerDAOS.add(CustomerConverter.toCustomerDAO(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCustomerDAOS;
    }

    @Override
    public List<Integer> getListOfValidIndexes() {

        List<Integer> indexes = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_LIST_OF_VALID_INDEXES)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                indexes.add(resultSet.getInt("customer_id"));
            }
            return indexes;
        } catch (SQLException e) {
            e.printStackTrace();
            return indexes;
        }
    }

    private boolean exists(CustomerDAO customerDAO) {
        return findAll().stream()
                .anyMatch(c -> c.equals(customerDAO));
    }

    private int getIdForExistingCustomer(CustomerDAO customerDAO) {
        return findAll().stream()
                .filter(c -> c.equals(customerDAO))
                .findFirst().orElse(new CustomerDAO()).getCustomerId();
    }

    private void setValuesAndExecutePreparedStatement(CustomerDAO customerDAO, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, customerDAO.getName());
        preparedStatement.setString(2, customerDAO.getCity());
        preparedStatement.executeUpdate();
    }

    private int getLastCustomerIndex() {

        int index = -1;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_LAST_CUSTOMER_INDEX)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                index = resultSet.getInt("customer_id");
            }
            return index;
        } catch (SQLException e) {
            e.printStackTrace();
            return index;
        }
    }
}
