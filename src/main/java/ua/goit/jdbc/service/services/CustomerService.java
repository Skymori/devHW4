package ua.goit.jdbc.service.services;


import ua.goit.jdbc.dao.CustomerDAO;
import ua.goit.jdbc.dao.ProjectDAO;
import ua.goit.jdbc.repositories.one_entity_repositories.Repository;
import ua.goit.jdbc.repositories.relations_repositories.RelationsRepository;
import ua.goit.jdbc.dto.CustomerDTO;
import ua.goit.jdbc.converters.CustomerConverter;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerService {
    private Repository<CustomerDAO> repository;
    private Repository<ProjectDAO> projectRepository;
    private RelationsRepository customersProjectsRepository;

    public CustomerService(Repository<CustomerDAO> repository, Repository<ProjectDAO> projectRepository, RelationsRepository customersProjectsRepository) {
        this.repository = repository;
        this.projectRepository = projectRepository;
        this.customersProjectsRepository = customersProjectsRepository;
    }

    public CustomerDTO create(CustomerDTO customerDTO) {
        CustomerDAO customerDAO = CustomerConverter.toCustomerDAO(customerDTO);
        CustomerDAO createdCustomerDa0 = repository.create(customerDAO);
        addProjectsAndRelations(customerDTO, createdCustomerDa0);
        return CustomerConverter.fromCustomerDAO(createdCustomerDa0);
    }

    public CustomerDTO findById(int customerId) {
        return CustomerConverter.fromCustomerDAO(repository.findById(customerId));
    }

    public CustomerDTO update(CustomerDTO customerDTO) {
        List<ProjectDAO> ProjectsToBeDeleted = getProjectsToBeDeleted(customerDTO);
        CustomerDAO customerDAO = CustomerConverter.toCustomerDAO(customerDTO);
        CustomerDAO updatedCustomerDao = repository.update(customerDAO);
        addProjectsAndRelations(customerDTO, updatedCustomerDao);
        ProjectsToBeDeleted.forEach(projectDAO -> customersProjectsRepository.delete(customerDAO.getCustomerId(), projectDAO.getProjectId()));
        return CustomerConverter.fromCustomerDAO(updatedCustomerDao);
    }

    public CustomerDTO deleteById(int customerId) {
        this.findById(customerId).getProjects().stream().
                forEach(projectDAO -> customersProjectsRepository.delete(customerId, projectDAO.getProjectId()));
        return CustomerConverter.fromCustomerDAO(repository.deleteById(customerId));
    }

    public CustomerDTO deleteByObject(CustomerDTO customerDTO) {
        return deleteById(customerDTO.getCustomerId());
    }

    public List<CustomerDTO> findAll() {
        return CustomerConverter.allFromCustomerDAO(repository.findAll());
    }

    private void addProjectsAndRelations(CustomerDTO customerTo, CustomerDAO createdCustomerDao) {
        List<ProjectDAO> projects = customerTo.getProjects();
        projects.forEach(p -> projectRepository.create(p));
        projects.stream().forEach(p -> customersProjectsRepository.create(createdCustomerDao.getCustomerId(), p.getProjectId()));
    }

    private List<ProjectDAO> getProjectsToBeDeleted(CustomerDTO customerDTO) {
        List<ProjectDAO> projectsOld = this.findById(customerDTO.getCustomerId()).getProjects();
        List<ProjectDAO> projectsNew = customerDTO.getProjects();
        return projectsOld.stream()
                .filter(projectDAO -> !projectsNew.contains(projectDAO))
                .collect(Collectors.toList());
    }
}
