package ua.goit.jdbc.service.services;



import ua.goit.jdbc.dao.CompanyDAO;
import ua.goit.jdbc.dao.CustomerDAO;
import ua.goit.jdbc.dao.DeveloperDAO;
import ua.goit.jdbc.dao.ProjectDAO;
import ua.goit.jdbc.repositories.one_entity_repositories.Repository;
import ua.goit.jdbc.repositories.relations_repositories.RelationsRepository;
import ua.goit.jdbc.dto.ProjectDTO;
import ua.goit.jdbc.converters.ProjectConverter;

import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
    private Repository<ProjectDAO> projectRepository;
    private Repository<DeveloperDAO> developerRepository;
    private Repository<CompanyDAO> companyRepository;
    private Repository<CustomerDAO> customerRepository;
    private RelationsRepository developersProjectsRepository;
    private RelationsRepository companiesProjectsRepository;
    private RelationsRepository customersProjectsRepository;

    public ProjectService(Repository<ProjectDAO> repository, Repository<DeveloperDAO> developerRepository,
                          Repository<CompanyDAO> companyRepository, Repository<CustomerDAO> customerRepository,
                          RelationsRepository developersProjectsRepository, RelationsRepository companiesProjectsRepository,
                          RelationsRepository customersProjectsRepository) {
        this.projectRepository = repository;
        this.developerRepository = developerRepository;
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.developersProjectsRepository = developersProjectsRepository;
        this.companiesProjectsRepository = companiesProjectsRepository;
        this.customersProjectsRepository = customersProjectsRepository;
    }

    public ProjectDTO create(ProjectDTO projectDTO) {
        ProjectDAO projectDAO = ProjectConverter.toProjectDAO(projectDTO);
        ProjectDAO createdProjectDAO = projectRepository.create(projectDAO);
        addDeveloperAndRelations(projectDTO, createdProjectDAO);
        addCompanyAndRelations(projectDTO, createdProjectDAO);
        addCustomerAndRelations(projectDTO, createdProjectDAO);
        return ProjectConverter.fromProjectDAO(createdProjectDAO);
    }

    public ProjectDTO findById(int projectId) {
        return ProjectConverter.fromProjectDAO(projectRepository.findById(projectId));
    }

    public ProjectDTO update(ProjectDTO projectDTO) {
        List<DeveloperDAO> developersToBeDeleted = getDevelopersToBeDeleted(projectDTO);
        List<CustomerDAO> customersToBeDeleted = getCustomersToBeDeleted(projectDTO);
        List<CompanyDAO> companiesToBeDeleted = getCompaniesToBeDeleted(projectDTO);
        ProjectDAO projectDAO = ProjectConverter.toProjectDAO(projectDTO);
        ProjectDAO updatedProject = projectRepository.update(projectDAO);
        addDeveloperAndRelations(projectDTO, updatedProject);
        addCompanyAndRelations(projectDTO, updatedProject);
        addCustomerAndRelations(projectDTO, updatedProject);
        developersToBeDeleted.forEach(developer -> developersProjectsRepository.delete(developer.getDeveloperId(), projectDTO.getProjectId()));
        customersToBeDeleted.forEach(customerDAO -> customersProjectsRepository.delete(customerDAO.getCustomerId(), projectDTO.getProjectId()));
        companiesToBeDeleted.forEach(companyDAO -> companiesProjectsRepository.delete(companyDAO.getCompanyId(), projectDTO.getProjectId()));
        ProjectDAO updatedProjectDAO = projectRepository.findById(projectDAO.getProjectId());
        return ProjectConverter.fromProjectDAO(updatedProjectDAO);
    }

    public ProjectDTO deletedById(int projectId) {
        deleteRelationsWithDevelopersCustomerCompanies(projectId);
        return ProjectConverter.fromProjectDAO(projectRepository.deleteById(projectId));
    }

    public ProjectDTO deletedByObject(ProjectDTO projectDTO) {
        return deletedById(projectDTO.getProjectId());
    }

    public List<ProjectDTO> findAll() {
        return ProjectConverter.allFromProjectDAO(projectRepository.findAll());
    }

    private void addCustomerAndRelations(ProjectDTO projectDTO, ProjectDAO createdProjectDao) {
        List<CustomerDAO> customers = projectDTO.getCustomers();
        customers.forEach(customerDAO -> customerRepository.create(customerDAO));
        customers.stream().forEach(customerDAO -> customersProjectsRepository.create(customerDAO.getCustomerId(), createdProjectDao.getProjectId()));
    }

    private void addCompanyAndRelations(ProjectDTO projectDTO, ProjectDAO createdProjectDAO) {
        List<CompanyDAO> companies = projectDTO.getCompanies();
        companies.forEach(companyDAO -> companyRepository.create(companyDAO));
        companies.stream().forEach(companyDAO -> companiesProjectsRepository.create(companyDAO.getCompanyId(), createdProjectDAO.getProjectId()));
    }

    private void addDeveloperAndRelations(ProjectDTO projectDTO, ProjectDAO createdProjectDAO) {
        List<DeveloperDAO> developers = projectDTO.getDevelopers();
        developers.forEach(developerDAO -> developerRepository.create(developerDAO));
        developers.stream().forEach(developerDAO -> developersProjectsRepository.create(developerDAO.getDeveloperId(), createdProjectDAO.getProjectId()));
    }

    private void deleteRelationsWithDevelopersCustomerCompanies(int projectId) {
        ProjectDTO projectDTO = findById(projectId);
        projectDTO.getDevelopers().forEach(developerDAO -> developersProjectsRepository.delete(developerDAO.getDeveloperId(), projectId));
        projectDTO.getCompanies().forEach(companyDAO -> companiesProjectsRepository.delete(companyDAO.getCompanyId(), projectId));
        projectDTO.getCustomers().forEach(customerDAO -> customersProjectsRepository.delete(customerDAO.getCustomerId(), projectId));
    }

    private List<DeveloperDAO> getDevelopersToBeDeleted(ProjectDTO projectDTO) {
        List<DeveloperDAO> developersOld = findById(projectDTO.getProjectId()).getDevelopers();
        List<DeveloperDAO> developersNew = projectDTO.getDevelopers();
        return developersOld.stream()
                .filter(developerDAO -> !developersNew.contains(developerDAO))
                .collect(Collectors.toList());
    }

    private List<CustomerDAO> getCustomersToBeDeleted(ProjectDTO projectDTO) {
        List<CustomerDAO> customersOld = findById(projectDTO.getProjectId()).getCustomers();
        List<CustomerDAO> customersNew = projectDTO.getCustomers();
        return customersOld.stream()
                .filter(developerDAO -> !customersNew.contains(developerDAO))
                .collect(Collectors.toList());
    }

    private List<CompanyDAO> getCompaniesToBeDeleted(ProjectDTO projectDTO) {
        List<CompanyDAO> companiesOld = findById(projectDTO.getProjectId()).getCompanies();
        List<CompanyDAO> companiesNew = projectDTO.getCompanies();
        return companiesOld.stream()
                .filter(developerDAO -> !companiesNew.contains(developerDAO))
                .collect(Collectors.toList());
    }

    public List<Integer> getListOfValidIndexes() {
        return projectRepository.getListOfValidIndexes();
    }
}
