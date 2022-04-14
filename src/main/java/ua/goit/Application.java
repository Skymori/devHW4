package ua.goit;

import ua.goit.jdbc.config.DatabaseManagerConnection;
import ua.goit.jdbc.repositories.one_entity_repositories.*;
import ua.goit.jdbc.repositories.relations_repositories.CompaniesProjectsRepository;
import ua.goit.jdbc.repositories.relations_repositories.CustomersProjectsRepository;
import ua.goit.jdbc.repositories.relations_repositories.DevelopersProjectsRepository;
import ua.goit.jdbc.repositories.relations_repositories.DevelopersSkillsRepository;
import ua.goit.jdbc.util.CommandProcessor;
import ua.goit.jdbc.service.services.ProjectService;
import ua.goit.jdbc.util.CommandUtil;
import ua.goit.jdbc.util.DefaultUtil;
import ua.goit.jdbc.config.PropertiesConfig;
import ua.goit.view.Console;
import ua.goit.view.View;


public class Application {
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

        ProjectService projectService = new ProjectService(projectRepository, developerRepository, companyRepository, customerRepository,
                developersProjectsRepository,companiesProjectsRepository, customersProjectsRepository);
        ua.goit.jdbc.service.services.CompanyService companyService = new ua.goit.jdbc.service.services.CompanyService(companyRepository, projectRepository, companiesProjectsRepository);
        ua.goit.jdbc.service.services.DeveloperService developerService = new ua.goit.jdbc.service.services.DeveloperService(developerRepository, projectRepository, companyRepository,
                skillRepository, developersProjectsRepository, developersSkillsRepository);

        CommandUtil commandUtil = new CommandUtil(projectService, companyService, developerService);
        View view = new Console();
        DefaultUtil defaultUtil = new DefaultUtil();

        CommandProcessor commandProcessor = new CommandProcessor(commandUtil, defaultUtil, view);
        commandProcessor.process();
    }
}
