package ua.goit.jdbc.util;


import ua.goit.jdbc.config.DatabaseManagerConnection;
import ua.goit.jdbc.config.PropertiesConfig;
import ua.goit.jdbc.converters.*;
import ua.goit.jdbc.dao.*;
import ua.goit.jdbc.repositories.one_entity_repositories.CompanyRepository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RelationsUtils {
    private static final DatabaseManagerConnection CONNECTION_MANAGER;

    static {
        PropertiesConfig propertiesConfig = new PropertiesConfig("application.properties");
        CONNECTION_MANAGER = new DatabaseManagerConnection(propertiesConfig);
    }

    public static List<ProjectDAO> getAllProjectsForCompany(int companyId) {
        String getAllProjectsForCompany = "SELECT p.project_id, p.name, p.description, p.cost, p.creation_date FROM projects p, companies c, companies_projects cp " +
                "WHERE p.project_id=cp.project_id AND c.company_id=cp.company_id AND c.company_id=?";
        List<ProjectDAO> projects = new ArrayList<>();
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getAllProjectsForCompany)) {
            preparedStatement.setInt(1, companyId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                projects.add(ProjectConverter.toProjectDAO(resultSet));
            }
            return projects;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<ProjectDAO> getAllProjectsForCustomer(int customerId) {
        String getAllProjectsForCustomer = "SELECT p.project_id, p.name, p.description, p.cost, p.creation_date  FROM projects p, customers c, customers_projects cp " +
                "WHERE p.project_id=cp.project_id AND c.customer_id=cp.customer_id AND c.customer_id=?";
        List<ProjectDAO> projects = new ArrayList<>();
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getAllProjectsForCustomer)) {
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                projects.add(ProjectConverter.toProjectDAO(resultSet));
            }
            return projects;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static CompanyDAO getCompanyForDeveloper(int companyId) {
        CompanyRepository companyRepository = new CompanyRepository(CONNECTION_MANAGER);
        return companyRepository.findById(companyId);
    }

    public static List<ProjectDAO> getAllProjectsForDeveloper(int developerId) {
        String getAllProjectsForDeveloper = "SELECT p.project_id, p.name, p.description, p.cost, p.creation_date  FROM projects p, developers d, developers_projects dp " +
                "WHERE p.project_id=dp.project_id AND d.developer_id=dp.developer_id AND d.developer_id=?";
        List<ProjectDAO> projects = new ArrayList<>();
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getAllProjectsForDeveloper)) {
            preparedStatement.setInt(1, developerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                projects.add(ProjectConverter.toProjectDAO(resultSet));
            }
            return projects;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<SkillDAO> getAllSkillsForDeveloper(int developerId) {
        String getAllSkillsForDeveloper = "SELECT s.skill_id, s.language, s.level FROM skills s, developers d, developers_skills ds " +
                "WHERE s.skill_id=ds.skill_id AND d.developer_id=ds.developer_id AND d.developer_id=?";
        List<SkillDAO> skills = new ArrayList<>();
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getAllSkillsForDeveloper)) {
            preparedStatement.setInt(1, developerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                skills.add(SkillConverter.toSkillDAO(resultSet));
            }
            return skills;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<CompanyDAO> getAllCompaniesForProject(int projectId) {
        String getAllCompaniesForProject = "SELECT c.company_id, c.name, c.city FROM companies c, projects p, companies_projects cp " +
                "WHERE c.company_id=cp.company_id AND p.project_id=cp.project_id AND p.project_id=?";
        List<CompanyDAO> companies = new ArrayList<>();
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getAllCompaniesForProject)) {
            preparedStatement.setInt(1, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                companies.add(CompanyConverter.toCompanyDAO(resultSet));
            }
            return companies;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<CustomerDAO> getCustomerForProject(int projectId) {
        String getCustomerForProject = "SELECT c.customer_id, c.name, c.city FROM customers c, projects p, customers_projects cp " +
                "WHERE c.customer_id=cp.customer_id AND p.project_id=cp.project_id AND p.project_id=?";
        List<CustomerDAO> customers = new ArrayList<>();
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getCustomerForProject)) {
            preparedStatement.setInt(1, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                customers.add(CustomerConverter.toCustomerDAO(resultSet));
            }
            return customers;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<DeveloperDAO> getAllDevelopersForProject(int projectId) {
        String getAllDevelopersForProject = "SELECT d.developer_id, d.name, d.age, d.sex, d.id_company, d.salary FROM developers d, projects p, developers_projects dp " +
                "WHERE p.project_id=dp.project_id AND d.developer_id=dp.developer_id AND p.project_id=?";
        List<DeveloperDAO> developerDAOS = new ArrayList<>();
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getAllDevelopersForProject)) {
            preparedStatement.setInt(1, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                developerDAOS.add(DeveloperConverter.toDeveloperDAO(resultSet));
            }
            return developerDAOS;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<DeveloperDAO> getAllDevelopersWithSkillLanguage(String skillLanguage) {
        String getAllDevelopersWithSkillLanguage = "SELECT d.developer_id, d.name, d.age, d.sex, d.company_id, d.salary FROM developers d, skills s, developers_skills ds " +
                "WHERE d.developer_id = ds.developer_id AND s.skill_id=ds.skill_id AND s.language=?::language_choice";
        List<DeveloperDAO> developerDAOS = new ArrayList<>();
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getAllDevelopersWithSkillLanguage)) {
            preparedStatement.setString(1, skillLanguage);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                developerDAOS.add(DeveloperConverter.toDeveloperDAO(resultSet));
            }
            return developerDAOS;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<DeveloperDAO> getAllDevelopersWithSkillLevel(String skillLevel) {
        String getAllDevelopersWithSkillLevel = "SELECT d.developer_id, d.name, d.age, d.sex, d.id_company, d.salary FROM developers d, skills s, developers_skills ds" +
                " WHERE d.developer_id = ds.developer_id AND s.skill_id=ds.skill_id AND s.level=?::level_choice";
        List<DeveloperDAO> developerDAOS = new ArrayList<>();
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getAllDevelopersWithSkillLevel)) {
            preparedStatement.setString(1, skillLevel);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                developerDAOS.add(DeveloperConverter.toDeveloperDAO(resultSet));
            }
            return developerDAOS;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
