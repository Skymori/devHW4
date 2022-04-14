package ua.goit.jdbc.converters;


import ua.goit.jdbc.dao.DeveloperDAO;
import ua.goit.jdbc.dto.DeveloperDTO;
import ua.goit.jdbc.util.RelationsUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class DeveloperConverter {

    public static DeveloperDAO toDeveloperDAO(DeveloperDTO developerDTO) {
        DeveloperDAO developerDAO = new DeveloperDAO();
        developerDAO.setDeveloperId(developerDTO.getDeveloperId());
        developerDAO.setName(developerDTO.getName());
        developerDAO.setAge(developerDTO.getAge());
        developerDAO.setSex(developerDTO.getSex());
        developerDAO.setSalary(developerDTO.getSalary());
        developerDAO.setCompanyId(developerDTO.getCompanyDAO().getCompanyId());
        return developerDAO;
    }

    public static DeveloperDTO fromDeveloperDAO(DeveloperDAO developerDAO) {
        DeveloperDTO developerDTO = new DeveloperDTO();
        developerDTO.setDeveloperId(developerDAO.getDeveloperId());
        developerDTO.setName(developerDAO.getName());
        developerDTO.setAge(developerDAO.getAge());
        developerDTO.setSex(developerDAO.getSex());
        developerDTO.setSalary(developerDAO.getSalary());
        developerDTO.setCompanyDAO(RelationsUtils.getCompanyForDeveloper(developerDAO.getCompanyId()));
        developerDTO.setProjects(RelationsUtils.getAllProjectsForDeveloper(developerDAO.getDeveloperId()));
        developerDTO.setSkills(RelationsUtils.getAllSkillsForDeveloper(developerDAO.getDeveloperId()));
        return developerDTO;
    }


    public static DeveloperDAO toDeveloperDAO(ResultSet resultSet) throws SQLException {
        DeveloperDAO developerDAO = new DeveloperDAO();
        developerDAO.setDeveloperId(resultSet.getInt("developer_id"));
        developerDAO.setName(resultSet.getString("name"));
        developerDAO.setAge(resultSet.getInt("age"));
        developerDAO.setSex(resultSet.getString("sex"));
        developerDAO.setSalary(resultSet.getDouble("salary"));
        developerDAO.setCompanyId(resultSet.getInt("company_id"));
        return developerDAO;
    }

    public static List<DeveloperDTO> allFromDeveloperDao(List<DeveloperDAO> developerDaos) {
        return developerDaos.stream()
                .map(DeveloperConverter::fromDeveloperDAO)
                .collect(Collectors.toList());
    }
}
