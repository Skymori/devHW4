package ua.goit.jdbc.converters;

import ua.goit.jdbc.dao.CompanyDAO;
import ua.goit.jdbc.dto.CompanyDTO;
import ua.goit.jdbc.util.RelationsUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyConverter {

    public static CompanyDAO toCompanyDAO(CompanyDTO companyDTO) {
        CompanyDAO companyDAO = new CompanyDAO();
        companyDAO.setCompanyId(companyDTO.getCompanyId());
        companyDAO.setName(companyDTO.getName());
        companyDAO.setCity(companyDTO.getCity());
        return companyDAO;
    }

    public static CompanyDTO fromCompanyDAO(CompanyDAO companyDAO) {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyId(companyDAO.getCompanyId());
        companyDTO.setName(companyDAO.getName());
        companyDTO.setCity(companyDAO.getCity());
        companyDTO.setProjects(RelationsUtils.getAllProjectsForCompany(companyDAO.getCompanyId()));
        return companyDTO;
    }

    public static CompanyDAO toCompanyDAO(ResultSet resultSet) throws SQLException {
        CompanyDAO companyDAO = new CompanyDAO();
        companyDAO.setCompanyId(resultSet.getInt("company_id"));
        companyDAO.setName(resultSet.getString("name"));
        companyDAO.setCity(resultSet.getString("city"));
        return companyDAO;
    }

    public static List<CompanyDTO> allFromCompanyDAO(List<CompanyDAO> companyDAOS) {
        return companyDAOS.stream()
                .map(CompanyConverter::fromCompanyDAO)
                .collect(Collectors.toList());
    }
}
