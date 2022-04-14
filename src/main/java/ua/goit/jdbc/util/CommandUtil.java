package ua.goit.jdbc.util;



import ua.goit.jdbc.dao.DeveloperDAO;
import ua.goit.jdbc.dto.CompanyDTO;
import ua.goit.jdbc.dto.DeveloperDTO;
import ua.goit.jdbc.dto.ProjectDTO;
import ua.goit.jdbc.service.services.ProjectService;

import java.util.List;
import java.util.stream.Collectors;

public class CommandUtil {

    ProjectService projectService;
    ua.goit.jdbc.service.services.CompanyService companyService;
    ua.goit.jdbc.service.services.DeveloperService developerService;

    public CommandUtil(ProjectService projectService, ua.goit.jdbc.service.services.CompanyService companyService, ua.goit.jdbc.service.services.DeveloperService developerService) {
        this.projectService = projectService;
        this.companyService = companyService;
        this.developerService = developerService;
    }

    public double getAllSalariesForProject(int id) {
        return getListOfDevelopersForProject(id).stream()
                .mapToDouble(DeveloperDAO::getSalary)
                .sum();
    }

    public List<DeveloperDAO> getListOfDevelopersForProject(int id) {
        ProjectDTO projectTo = projectService.findById(id);
        return projectTo.getDevelopers();
    }

    public List<DeveloperDAO> getListOfJavaDevelopers() {
        return RelationsUtils.getAllDevelopersWithSkillLanguage("Java");
    }

    public List<DeveloperDAO> getListOfMiddleDevelopers() {
        return RelationsUtils.getAllDevelopersWithSkillLevel("Middle");
    }

    public List<String> getShortDescriptionOfAllProjects() {
        return projectService.findAll().stream()
                .map(p -> p.getDate().toString() + " - " + p.getName() + " - " + p.getDevelopers().size() + ".")
                .collect(Collectors.toList());
    }

    public CompanyDTO consoleCompanyCreate(String name, String city, CompanyDTO defaultCompanyTo){
        return companyService.consoleCreate(name, city, defaultCompanyTo);
    }

    public CompanyDTO consoleCompanyUpdate(String name, String city, int id){
        return companyService.consoleUpdate(name, city, id);
    }

    public CompanyDTO findCompanyById(int id) {
        return companyService.findById(id);
    }

    public List<CompanyDTO> findAllCompanies(){
        return companyService.findAll();
    }

    public CompanyDTO deleteCompanyById(int id) {
        return companyService.deleteById(id);
    }

    public DeveloperDTO consoleDeveloperCreate(String name, int age, String sex, int companyId, double salary, DeveloperDTO defaultDeveloperDTO){
        return developerService.createConsole(name, age, sex, companyId, salary, defaultDeveloperDTO, companyService);
    }

    public DeveloperDTO consoleDeveloperUpdate(String name, int age, String sex, int companyId, double salary, int id){
        return developerService.updateConsole(name, age, sex, companyId, salary, id, companyService);
    }

    public DeveloperDTO findDeveloperById(int id) {
        return developerService.findById(id);
    }

    public List<DeveloperDTO> findAllDevelopers() {
        return developerService.findAll();
    }

    public DeveloperDTO deleteDeveloperById(int id) {
        return developerService.deleteById(id);
    }

    public List<Integer> getListOfValidIndexesForProject(){
        return projectService.getListOfValidIndexes();
    }

    public List<Integer> getListOfValidIndexesForCompany(){
        return companyService.getListOfValidIndexes();
    }

    public List<Integer> getListOfValidIndexesForDeveloper(){
        return developerService.getListOfValidIndexes();
    }
}
