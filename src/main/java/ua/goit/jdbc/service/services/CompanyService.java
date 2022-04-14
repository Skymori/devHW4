package ua.goit.jdbc.service.services;

import ua.goit.jdbc.dao.CompanyDAO;
import ua.goit.jdbc.dao.ProjectDAO;
import ua.goit.jdbc.repositories.one_entity_repositories.Repository;
import ua.goit.jdbc.repositories.relations_repositories.RelationsRepository;
import ua.goit.jdbc.dto.CompanyDTO;
import ua.goit.jdbc.converters.CompanyConverter;

import java.util.List;
import java.util.stream.Collectors;

public class CompanyService {
    private Repository<CompanyDAO> companyRepository;
    private Repository<ProjectDAO> projectRepository;
    private RelationsRepository companiesProjectsRepository;

    public CompanyService(Repository<CompanyDAO> companyRepository, Repository<ProjectDAO> projectRepository,
                          RelationsRepository companiesProjectsRepository) {
        this.companyRepository = companyRepository;
        this.projectRepository = projectRepository;
        this.companiesProjectsRepository = companiesProjectsRepository;
    }

    public CompanyDTO create(CompanyDTO companyDTO) {
        CompanyDAO companyDAO = CompanyConverter.toCompanyDAO(companyDTO);
        CompanyDAO createdCompanyDao = companyRepository.create(companyDAO);
        addProjectsAndRelations(companyDTO, createdCompanyDao);
        return CompanyConverter.fromCompanyDAO(createdCompanyDao);
    }

    public CompanyDTO consoleCreate(String name, String city, CompanyDTO defaultCompanyDTO){
        defaultCompanyDTO.setName(name);
        defaultCompanyDTO.setCity(city);
        return create(defaultCompanyDTO);
    }


    public CompanyDTO findById(int companyId) {
        return CompanyConverter.fromCompanyDAO(companyRepository.findById(companyId));
    }

    public CompanyDTO update(CompanyDTO companyDTO) {
        List<ProjectDAO> projectsToBeDeleted = getProjectsToBeDeleted(companyDTO);
        CompanyDAO companyDAO = CompanyConverter.toCompanyDAO(companyDTO);
        CompanyDAO updatedCompanyDao = companyRepository.update(companyDAO);
        addProjectsAndRelations(companyDTO, updatedCompanyDao);
        projectsToBeDeleted.forEach(projectDao -> companiesProjectsRepository.delete(companyDTO.getCompanyId(), projectDao.getProjectId()));
        return CompanyConverter.fromCompanyDAO(updatedCompanyDao);
    }

    public CompanyDTO consoleUpdate(String name, String city, int id){
        CompanyDTO companyDTO = findById(id);
        companyDTO.setName(name);
        companyDTO.setCity(city);
        return update(companyDTO);
    }

    public CompanyDTO deleteById(int companyId) {
        this.findById(companyId).getProjects().stream().
                forEach(projectDAO -> companiesProjectsRepository.delete(companyId, projectDAO.getProjectId()));
        return CompanyConverter.fromCompanyDAO(companyRepository.deleteById(companyId));
    }

    public CompanyDTO deleteByObject(CompanyDTO companyDTO) {
        return deleteById(companyDTO.getCompanyId());
    }

    public List<CompanyDTO> findAll() {
        return CompanyConverter.allFromCompanyDAO(companyRepository.findAll());
    }

    private void addProjectsAndRelations(CompanyDTO companyDTO, CompanyDAO createdCompanyDAO) {
        List<ProjectDAO> projects = companyDTO.getProjects();
        projects.forEach(p -> projectRepository.create(p));
        projects.stream().forEach(p -> companiesProjectsRepository.create(createdCompanyDAO.getCompanyId(), p.getProjectId()));
    }

    private List<ProjectDAO> getProjectsToBeDeleted(CompanyDTO companyDTO) {
        List<ProjectDAO> projectsOld = findById(companyDTO.getCompanyId()).getProjects();
        List<ProjectDAO> projectsNew = companyDTO.getProjects();
        return projectsOld.stream()
                .filter(projectDAO -> !projectsNew.contains(projectDAO))
                .collect(Collectors.toList());
    }

    public List<Integer> getListOfValidIndexes() {
        return companyRepository.getListOfValidIndexes();
    }
}
