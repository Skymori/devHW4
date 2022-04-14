package ua.goit.jdbc;


import ua.goit.jdbc.config.DatabaseManagerConnection;
import ua.goit.jdbc.repositories.one_entity_repositories.*;
import ua.goit.jdbc.repositories.relations_repositories.CompaniesProjectsRepository;
import ua.goit.jdbc.repositories.relations_repositories.CustomersProjectsRepository;
import ua.goit.jdbc.repositories.relations_repositories.DevelopersProjectsRepository;
import ua.goit.jdbc.repositories.relations_repositories.DevelopersSkillsRepository;
import ua.goit.jdbc.config.PropertiesConfig;

public class Main {
    public static void main(String[] args) {
        PropertiesConfig propertiesConfig = new PropertiesConfig("application.properties");
        DatabaseManagerConnection cm = new DatabaseManagerConnection(propertiesConfig);

        ProjectRepository projectRepository = new ProjectRepository(cm);
        CompanyRepository companyRepository = new CompanyRepository(cm);
        CustomerRepository customerRepository = new CustomerRepository(cm);
        DeveloperRepository developerRepository = new DeveloperRepository(cm);
        SkillRepository skillRepository = new SkillRepository(cm);
        CompaniesProjectsRepository companiesProjectsRepository = new CompaniesProjectsRepository(cm);
        CustomersProjectsRepository customersProjectsRepository = new CustomersProjectsRepository(cm);
        DevelopersProjectsRepository developersProjectsRepository = new DevelopersProjectsRepository(cm);
        DevelopersSkillsRepository developersSkillsRepository = new DevelopersSkillsRepository(cm);


    }
}
