package ua.goit.jdbc.service.services;

import ua.goit.jdbc.dao.CompanyDAO;
import ua.goit.jdbc.dao.DeveloperDAO;
import ua.goit.jdbc.dao.ProjectDAO;
import ua.goit.jdbc.dao.SkillDAO;
import ua.goit.jdbc.repositories.one_entity_repositories.Repository;
import ua.goit.jdbc.repositories.relations_repositories.RelationsRepository;
import ua.goit.jdbc.dto.DeveloperDTO;
import ua.goit.jdbc.converters.CompanyConverter;
import ua.goit.jdbc.converters.DeveloperConverter;

import java.util.List;
import java.util.stream.Collectors;

public class DeveloperService {
    private Repository<DeveloperDAO> developerRepository;
    private Repository<ProjectDAO> projectRepository;
    private Repository<CompanyDAO> companyRepository;
    private Repository<SkillDAO> skillRepository;
    private RelationsRepository developersProjectsRepository;
    private RelationsRepository developersSkillRepository;

    public DeveloperService(Repository<DeveloperDAO> developerRepository,
                            Repository<ProjectDAO> projectRepository,
                            Repository<CompanyDAO> companyRepository,
                            Repository<SkillDAO> skillRepository,
                            RelationsRepository developersProjectsRepository,
                            RelationsRepository developersSkillRepository) {
        this.developerRepository = developerRepository;
        this.projectRepository = projectRepository;
        this.companyRepository = companyRepository;
        this.skillRepository = skillRepository;
        this.developersProjectsRepository = developersProjectsRepository;
        this.developersSkillRepository = developersSkillRepository;
    }

    public DeveloperDTO create(DeveloperDTO developerDTO) {
        addCompanyIfNotExists(developerDTO);
        DeveloperDAO createdDeveloperDAO = developerRepository.create(DeveloperConverter.toDeveloperDAO(developerDTO));
        addProjectsAndRelations(developerDTO, createdDeveloperDAO);
        addSkillsAndRelations(developerDTO, createdDeveloperDAO);
        return DeveloperConverter.fromDeveloperDAO(createdDeveloperDAO);
    }

    public DeveloperDTO createConsole(String name, int age, String sex, int companyId, double salary, DeveloperDTO defaultDeveloperDTO, CompanyService companyService){
        defaultDeveloperDTO.setName(name);
        defaultDeveloperDTO.setAge(age);
        defaultDeveloperDTO.setSex(sex);
        defaultDeveloperDTO.setCompanyDAO(CompanyConverter.toCompanyDAO(companyService.findById(companyId)));
        defaultDeveloperDTO.setSalary(salary);
        return create(defaultDeveloperDTO);
    }

    public DeveloperDTO findById(int developerId) {
        return DeveloperConverter.fromDeveloperDAO(developerRepository.findById(developerId));
    }

    public DeveloperDTO update(DeveloperDTO developerDTO) {
        List<ProjectDAO> projectsToBeDeleted = getProjectsToBeDeleted(developerDTO);
        DeveloperDAO developerDAO = DeveloperConverter.toDeveloperDAO(developerDTO);
        DeveloperDAO updatedDeveloperDAO = developerRepository.update(developerDAO);
        addProjectsAndRelations(developerDTO, updatedDeveloperDAO);
        projectsToBeDeleted.forEach(projectDAO -> developersProjectsRepository.delete(developerDTO.getDeveloperId(), projectDAO.getProjectId()));
        addSkillsAndRelations(developerDTO, updatedDeveloperDAO);
        return DeveloperConverter.fromDeveloperDAO(updatedDeveloperDAO);
    }

    public DeveloperDTO updateConsole(String name, int age, String sex, int companyId, double salary, int id, CompanyService companyService){
        DeveloperDTO developerDTO = findById(id);
        developerDTO.setName(name);
        developerDTO.setAge(age);
        developerDTO.setSex(sex);
        developerDTO.setCompanyDAO(CompanyConverter.toCompanyDAO(companyService.findById(companyId)));
        developerDTO.setSalary(salary);
        return update(developerDTO);
    }

    public DeveloperDTO deleteById(int developerId) {
        deleteRelationsWithProjectsAndSkills(developerId);
        return DeveloperConverter.fromDeveloperDAO(developerRepository.deleteById(developerId));
    }

    public DeveloperDTO deleteByObject(DeveloperDTO developerDTO) {
        return deleteById(developerDTO.getDeveloperId());
    }

    public List<DeveloperDTO> findAll() {
        List<DeveloperDAO> allCompaniesDAO = developerRepository.findAll();
        return DeveloperConverter.allFromDeveloperDao(allCompaniesDAO);
    }

    private void addProjectsAndRelations(DeveloperDTO developerDTO, DeveloperDAO createdDeveloperDAO) {
        List<ProjectDAO> projects = developerDTO.getProjects();
        projects.forEach(p -> projectRepository.create(p));
        projects.forEach(p -> developersProjectsRepository.create(createdDeveloperDAO.getDeveloperId(), p.getProjectId()));
    }

    private List<ProjectDAO> getProjectsToBeDeleted(DeveloperDTO developerDTO) {
        List<ProjectDAO> projectsOld = this.findById(developerDTO.getDeveloperId()).getProjects();
        List<ProjectDAO> projectsNew = developerDTO.getProjects();
        return projectsOld.stream()
                .filter(projectDAO -> !projectsNew.contains(projectDAO))
                .collect(Collectors.toList());
    }

    private void addSkillsAndRelations(DeveloperDTO developerDTO, DeveloperDAO createdDeveloperDAO) {
        List<SkillDAO> skills = developerDTO.getSkills();
        skills.forEach(skillDAO -> skillRepository.create(skillDAO));
        skills.forEach(skillDAO -> developersSkillRepository.create(createdDeveloperDAO.getDeveloperId(), skillDAO.getSkillId()));
    }

    private void addCompanyIfNotExists(DeveloperDTO developerDTO) {
        CompanyDAO companyDAO = developerDTO.getCompanyDAO();
        companyRepository.create(companyDAO);
    }

    private void deleteRelationsWithProjectsAndSkills(int developerId) {
        DeveloperDTO developerDTO = findById(developerId);
        developerDTO.getProjects().forEach(projectDAO -> developersProjectsRepository.delete(developerId, projectDAO.getProjectId()));
        developerDTO.getSkills().forEach(skillDAO -> developersSkillRepository.delete(developerId, skillDAO.getSkillId()));
    }

    public List<Integer> getListOfValidIndexes() {
        return developerRepository.getListOfValidIndexes();
    }
}
