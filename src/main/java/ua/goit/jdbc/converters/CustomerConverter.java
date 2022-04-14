package ua.goit.jdbc.converters;


import ua.goit.jdbc.dao.CustomerDAO;
import ua.goit.jdbc.dto.CustomerDTO;
import ua.goit.jdbc.util.RelationsUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerConverter {

    public static CustomerDAO toCustomerDAO(CustomerDTO customerDTO) {
        CustomerDAO customerDAO = new CustomerDAO();
        customerDAO.setCustomerId(customerDTO.getCustomerId());
        customerDAO.setName(customerDTO.getName());
        customerDAO.setCity(customerDTO.getCity());
        return customerDAO;
    }

    public static CustomerDTO fromCustomerDAO(CustomerDAO customerDAO) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomerId(customerDAO.getCustomerId());
        customerDTO.setName(customerDAO.getName());
        customerDTO.setCity(customerDAO.getCity());
        customerDTO.setProjects(RelationsUtils.getAllProjectsForCustomer(customerDAO.getCustomerId()));
        return customerDTO;
    }

    public static CustomerDAO toCustomerDAO(ResultSet resultSet) throws SQLException {
        CustomerDAO customerDAO = new CustomerDAO();
        customerDAO.setCustomerId(resultSet.getInt("customer_id"));
        customerDAO.setName(resultSet.getString("name"));
        customerDAO.setCity(resultSet.getString("city"));
        return customerDAO;
    }

    public static List<CustomerDTO> allFromCustomerDAO(List<CustomerDAO> customerDAOS) {
        return customerDAOS.stream()
                .map(CustomerConverter::fromCustomerDAO)
                .collect(Collectors.toList());
    }

}
