package ua.goit.jdbc.util;



import ua.goit.jdbc.dto.CompanyDTO;
import ua.goit.jdbc.dto.DeveloperDTO;
import ua.goit.view.View;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CommandProcessor {
    CommandUtil commandUtil;
    DefaultUtil defaultUtil;
    View view;

    public CommandProcessor(CommandUtil commandUtil, DefaultUtil defaultUtil, View view) {
        this.commandUtil = commandUtil;
        this.defaultUtil = defaultUtil;
        this.view = view;
    }

    private static String GREETING_MESSAGE = "Wellcome to our console application";
    private static String CHOICE_MESSAGE = " Please type the letter of one of the command:\n"
            + "q - to exit the application\n"
            + "l - to get the list of the command\n"
            + "s - to get the list of salaries for concrete project\n"
            + "d - to get the list of developers for concrete project\n"
            + "j - to get the list of all java developers\n"
            + "m - to get the list of all middle developers\n"
            + "p - to get the list of all projects\n"
            + "cc - to create the company\n"
            + "fci - to find the company by id\n"
            + "cu - to update the company\n"
            + "cd - to delete the company\n"
            + "fc - to get list of all companies\n"
            + "dc - to create the developer\n"
            + "fdi - to find the developer by id\n"
            + "du - to update the developer\n"
            + "dd - to delete the developer\n"
            + "fd - to get list of all developers";


    public void process() {
        view.write(GREETING_MESSAGE);
        boolean isNotExit = true;
        boolean isChosen = false;
        while (isNotExit) {
            view.write(CHOICE_MESSAGE);
            String input = view.read();
            if (input.equalsIgnoreCase("q")) {
                view.write("You are leaving our application. Good Bye.");
                isNotExit = false;
                isChosen = true;
            }

            while (!isChosen) {
                isChosen = chooseCommand(input);
            }
            isChosen = false;
        }
    }

    private boolean chooseCommand(String message) {
        int id;
        switch (message.toLowerCase()) {
            case "s":
                id = getIdFromUserForProject();
                if (id == -1) {
                    return true;
                }
                view.write("The hole sum of salaries per this project is " + commandUtil.getAllSalariesForProject(id) + "$");
                view.write("");
                return true;
            case "d":
                id = getIdFromUserForProject();
                if (id == -1) {
                    return true;
                }
                view.write("The list of developers per this project:");
                commandUtil.getListOfDevelopersForProject(id).forEach(System.out::println);
                view.write("");
                return true;
            case "j":
                view.write("The list of all java developers: ");
                commandUtil.getListOfJavaDevelopers().forEach(System.out::println);
                view.write("");
                return true;
            case "m":
                view.write("The list of all middle developers");
                commandUtil.getListOfMiddleDevelopers().forEach(System.out::println);
                view.write("");
                return true;
            case "p":
                view.write("The list of all projects:");
                commandUtil.getShortDescriptionOfAllProjects().forEach(System.out::println);
                view.write("");
                return true;
            case "l":
                return true;
            case "cc":
                view.write("You created company: " + createCompany().toString());
                view.write("");
                return true;
            case "fci":
                id = getIdFromUserForCompany();
                if (id == -1) {
                    return true;
                }
                view.write(commandUtil.findCompanyById(id).toString());
                view.write("");
                return true;
            case "cu":
                id = getIdFromUserForCompany();
                if (id == -1) {
                    return true;
                }
                view.write("You updated company with id=" + id + ": " + updateCompany(id).toString());
                return true;
            case "cd":
                id = getIdFromUserForCompany();
                if (id == -1) {
                    return true;
                }
                view.write("You deleted: " + commandUtil.deleteCompanyById(id));
                view.write("");
                return true;
            case "fc":
                commandUtil.findAllCompanies().forEach(c -> view.write(c.toString()));
                view.write("");
                return true;
            case "dc":
                view.write("You created developer: " + createDeveloper().toString());
                view.write("");
                return true;
            case "fdi":
                id = getIdFromUserForDeveloper();
                if (id == -1) {
                    return true;
                }
                view.write(commandUtil.findDeveloperById(id).toString());
                view.write("");
                return true;
            case "du":
                id = getIdFromUserForDeveloper();
                if (id == -1) {
                    return true;
                }
                view.write("You updated developer with id=" + id + ": " + updateDeveloper(id));
                view.write("");
                return true;
            case "dd":
                id = getIdFromUserForDeveloper();
                if (id == -1) {
                    return true;
                }
                view.write("You deleted: " + commandUtil.deleteDeveloperById(id));
                view.write("");
                return true;
            case "fd":
                commandUtil.findAllDevelopers().forEach(d -> view.write(d.toString()));
                view.write("");
                return true;
            default:
                view.write("You entered the invalid command, please try again");
                return true;
        }
    }

    private DeveloperDTO updateDeveloper(int id) {
        List<Integer> ages = IntStream.range(18, 120).boxed().collect(Collectors.toList());
        view.write("Please enter developer name");
        String name = view.read();
        view.write("Please enter developers age - an integer number");
        int age = getIntegerFromConsole(view.read(), ages);
        view.write("Please enter sex: male of female");
        String sex = getSexFromUser(view.read().toLowerCase());
        int companyId = getIdFromUserForCompany();
        view.write("Please enter salary");
        double salary = getSalaryFromUser(view.read());
        return commandUtil.consoleDeveloperUpdate(name, age, sex, companyId, salary, id);
    }

    private CompanyDTO createCompany() {
        CompanyDTO companyTo = defaultUtil.getDefaultCompanyDTO();
        view.write("Please enter company name");
        String name = view.read();
        view.write("Please enter company city");
        String city = view.read();
        return commandUtil.consoleCompanyCreate(name, city, companyTo);
    }

    private CompanyDTO updateCompany(int id) {
        view.write("Please enter company name");
        String name = view.read();
        view.write("Please enter company city");
        String city = view.read();
        return commandUtil.consoleCompanyUpdate(name, city, id);
    }

    private DeveloperDTO createDeveloper() {
        DeveloperDTO developerTo = defaultUtil.getDefaultDeveloperDTO1();
        List<Integer> ages = IntStream.range(18, 120).boxed().collect(Collectors.toList());
        view.write("Please enter developer name");
        String name = view.read();
        view.write("Please enter developers age - an integer number");
        int age = getIntegerFromConsole(view.read(), ages);
        view.write("Please enter sex: male of female");
        String sex = getSexFromUser(view.read().toLowerCase());
        int companyId = getIdFromUserForCompany();
        view.write("Please enter salary");
        double salary = getSalaryFromUser(view.read());
        return commandUtil.consoleDeveloperCreate(name, age, sex, companyId, salary, developerTo);
    }

    private String getSexFromUser(String input) {
        String errorMessage = "It's not the correct sex: please enter male or female";
        String sex = input.strip().toLowerCase();
        boolean isFieldBlank = true;
        while (isFieldBlank) {
            if (sex.equals("male") || sex.equals("female")) {
                isFieldBlank = false;
            } else {
                view.write(errorMessage);
                sex = view.read().strip().toLowerCase();
            }
        }
        return sex;
    }

    private double getSalaryFromUser(String input) {
        String errorMessage = "It's not a correct number for salary, please try again";
        input = input.replaceAll(",", ".");
        double number = 0;
        boolean isFieldBlank = true;
        while (isFieldBlank) {
            try {
                number = Double.parseDouble(input);
                if (number < 0) {
                    view.write(errorMessage);
                    input = view.read().replaceAll(",", ".");
                } else {
                    isFieldBlank = false;
                }
            } catch (Exception e) {
                view.write(errorMessage);
                input = view.read().replaceAll(",", ".");
            }
        }
        return number;
    }

    private int getIdFromUserForProject() {
        List<Integer> listOfValidIndexesForProject = commandUtil.getListOfValidIndexesForProject();
        if (!listOfValidIndexesForProject.isEmpty()) {
            view.write("Please enter the id of the project from the list: " + listOfValidIndexesForProject.toString());
            return getIntegerFromConsole(view.read(), listOfValidIndexesForProject);
        } else {
            view.write("There are no projects in the database available");
            return -1;
        }
    }

    private int getIdFromUserForDeveloper() {
        List<Integer> listOfValidIndexesForDeveloper = commandUtil.getListOfValidIndexesForDeveloper();
        if (!listOfValidIndexesForDeveloper.isEmpty()) {
            view.write("Please enter the id of the developer from the list: " + listOfValidIndexesForDeveloper.toString());
            return getIntegerFromConsole(view.read(), listOfValidIndexesForDeveloper);
        } else {
            view.write("There are no developers in the database available");
            return -1;
        }
    }

    private int getIdFromUserForCompany() {
        List<Integer> listOfValidIndexesForCompany = commandUtil.getListOfValidIndexesForCompany();
        if (!listOfValidIndexesForCompany.isEmpty()) {
            view.write("Please enter the id of the company from the list: " + listOfValidIndexesForCompany.toString());
            return getIntegerFromConsole(view.read(), listOfValidIndexesForCompany);
        } else {
            view.write("There are no developers in the database available");
            return -1;
        }
    }

    private int getIntegerFromConsole(String input, List<Integer> indexes) {
        String errorMessage = "It's not a correct number, please try again";
        int number = 0;
        boolean isFieldBlank = true;
        while (isFieldBlank) {
            try {
                number = Integer.parseInt(input);
                if (number <= 0 || !indexes.contains(number)) {
                    view.write(errorMessage);
                    input = view.read();
                } else {
                    isFieldBlank = false;
                }
            } catch (Exception e) {
                view.write(errorMessage);
                input = view.read();
            }
        }
        return number;
    }
}
