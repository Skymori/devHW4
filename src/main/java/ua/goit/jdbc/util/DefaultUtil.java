package ua.goit.jdbc.util;




import ua.goit.jdbc.dao.*;
import ua.goit.jdbc.dto.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DefaultUtil {

    private CompanyDTO defaultCompanyDTO;
    private CustomerDTO defaultCustomerDTO;
    private DeveloperDTO defaultDeveloperDTO1;
    private DeveloperDTO defaultDeveloperDTO2;
    private ProjectDTO defaultProjectDTO1;
    private ProjectDTO defaultProjectDTO2;
    private skillDTO defaultSkillDTO1;
    private skillDTO defaultSkillDTO2;

    public DefaultUtil() {

        defaultCompanyDTO = createDefaultCompanyDTO();
        defaultCustomerDTO = createDefaultCustomerDTO();
        defaultDeveloperDTO1 = createDefaultDeveloperDTO1();
        defaultDeveloperDTO2 = createDefaultDeveloperDTO2();
        defaultProjectDTO1 = createDefaultProjectDTO1();
        defaultProjectDTO2 = createDefaultProjectDTO2();
        defaultSkillDTO1 = createDefaultSkillDTO1();
        defaultSkillDTO2 = createDefaultSkillDTO2();
    }

    public CompanyDTO getDefaultCompanyDTO() {
        return defaultCompanyDTO;
    }

    public CustomerDTO getDefaultCustomerDTO() {
        return defaultCustomerDTO;
    }

    public DeveloperDTO getDefaultDeveloperDTO1() {
        return defaultDeveloperDTO1;
    }

    public DeveloperDTO getDefaultDeveloperDTO2() {
        return defaultDeveloperDTO2;
    }

    public ProjectDTO getDefaultProjectDTO1() {
        return defaultProjectDTO1;
    }

    public ProjectDTO getDefaultProjectDTO2() {
        return defaultProjectDTO2;
    }

    public skillDTO getDefaultSkillDTO1() {
        return defaultSkillDTO1;
    }

    public skillDTO getDefaultSkillDTO2() {
        return defaultSkillDTO2;
    }

    private DeveloperDTO createDefaultDeveloperDTO1() {
        DeveloperDTO developerDTO = new DeveloperDTO();
        developerDTO.setName("NewDeveloper1");
        developerDTO.setSalary(5700);
        developerDTO.setSex("male");
        developerDTO.setAge(38);
        CompanyDAO companyDAO = createCompanyDAO();
        developerDTO.setCompanyDAO(companyDAO);
        List<ProjectDAO> projectDAOS = createProjectDAOS();
        developerDTO.setProjects(projectDAOS);
        List<SkillDAO> skills = createSkillDAOS();
        developerDTO.setSkills(skills);
        return developerDTO;
    }

    private DeveloperDTO createDefaultDeveloperDTO2() {
        DeveloperDTO developerDTO = new DeveloperDTO();
        developerDTO.setName("NewDeveloper2");
        developerDTO.setSalary(5800);
        developerDTO.setSex("female");
        developerDTO.setAge(35);
        CompanyDAO companyDAO = createCompanyDAO();
        developerDTO.setCompanyDAO(companyDAO);
        List<ProjectDAO> projectDAOS = createProjectDAOS();
        developerDTO.setProjects(projectDAOS);
        List<SkillDAO> skills = createSkillDAOS();
        developerDTO.setSkills(skills);
        return developerDTO;
    }

    private List<SkillDAO> createSkillDAOS() {
        SkillDAO skillDAO = new SkillDAO();
        skillDAO.setLevel("Middle");
        skillDAO.setLanguage("Java");
        List<SkillDAO> skills = new ArrayList<>();
        skills.add(skillDAO);
        return skills;
    }

    private List<ProjectDAO> createProjectDAOS() {
        ProjectDAO projectDAO = new ProjectDAO();
        projectDAO.setName("TestProject");
        projectDAO.setDescription("Some description for testProject");
        projectDAO.setCost(6700);
        projectDAO.setProjectId(100);
        projectDAO.setDate(LocalDate.now());

        ProjectDAO projectDAO2 = new ProjectDAO();
        projectDAO2.setName("TestProject2");
        projectDAO2.setDescription("Some description for testProject2");
        projectDAO2.setCost(5700);
        projectDAO2.setProjectId(101);
        projectDAO2.setDate(LocalDate.now());

        List<ProjectDAO> projectDAOS = new ArrayList<>();
        projectDAOS.add(projectDAO);
        projectDAOS.add(projectDAO2);
        return projectDAOS;
    }

    private CompanyDAO createCompanyDAO() {
        CompanyDAO companyDAO = new CompanyDAO();
        companyDAO.setName("CompanyForDeveloper1");
        companyDAO.setCity("CityForDeveloper1");
        return companyDAO;
    }

    private skillDTO createDefaultSkillDTO1() {
        skillDTO skillDTO = new skillDTO();
        skillDTO.setLevel("Middle");
        skillDTO.setLanguage("Java");
        return skillDTO;
    }

    private skillDTO createDefaultSkillDTO2() {
        skillDTO skillDTO = new skillDTO();
        skillDTO.setLevel("Middle");
        skillDTO.setLanguage("C++");
        return skillDTO;
    }

    private ProjectDTO createDefaultProjectDTO1() {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("DefaultProject1");
        projectDTO.setDescription("Description of DefaultProject1");
        projectDTO.setDate(LocalDate.now());
        projectDTO.setCost(5700);
        projectDTO.setCustomers(createCustomerDAOS());
        projectDTO.setCompanies(createCompanyDAOS());
        projectDTO.setDevelopers(createDeveloperDAOS());
        projectDTO.setDate(LocalDate.now());
        return projectDTO;
    }


    private ProjectDTO createDefaultProjectDTO2() {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("DefaultProject2");
        projectDTO.setDescription("Description of DefaultProject2");
        projectDTO.setDate(LocalDate.now());
        projectDTO.setCost(5700);
        projectDTO.setCustomers(createCustomerDAOS());
        projectDTO.setCompanies(createCompanyDAOS());
        projectDTO.setDevelopers(createDeveloperDAOS());
        projectDTO.setDate(LocalDate.now());
        return projectDTO;
    }

    private List<DeveloperDAO> createDeveloperDAOS() {
        List<DeveloperDAO> developerDAOS = new ArrayList<>();
        DeveloperDAO developerDAO1 = new DeveloperDAO();
        developerDAO1.setName("NewDeveloper1");
        developerDAO1.setSalary(5800);
        developerDAO1.setSex("male");
        developerDAO1.setAge(38);
        developerDAOS.add(developerDAO1);
        DeveloperDAO developerDAO2 = new DeveloperDAO();
        developerDAO1.setName("NewDeveloper2");
        developerDAO1.setSalary(5700);
        developerDAO1.setSex("female");
        developerDAO1.setAge(35);
        developerDAOS.add(developerDAO2);
        return developerDAOS;
    }

    private List<CompanyDAO> createCompanyDAOS() {
        List<CompanyDAO> companyDAOS = new ArrayList<>();
        companyDAOS.add(createCompanyDAO());
        return companyDAOS;
    }

    private List<CustomerDAO> createCustomerDAOS() {
        CustomerDAO customerDAO1 = new CustomerDAO();
        customerDAO1.setName("CustomerDao1");
        customerDAO1.setCity("CustomerDaoCity1");
        CustomerDAO customerDAO2 = new CustomerDAO();
        customerDAO2.setName("CustomerDao2");
        customerDAO2.setCity("CustomerDaoCity2");
        List<CustomerDAO> customerDAOS = new ArrayList<>();
        customerDAOS.add(customerDAO1);
        customerDAOS.add(customerDAO2);
        return customerDAOS;
    }

    private CustomerDTO createDefaultCustomerDTO() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("NewCustomer");
        customerDTO.setCity("NewCity");
        customerDTO.setProjects(createProjectDAOS());
        return customerDTO;
    }

    private CompanyDTO createDefaultCompanyDTO() {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setName("NewCompany");
        companyDTO.setCity("NewCompanyCity");
        companyDTO.setProjects(createProjectDAOS());
        return companyDTO;
    }

}
