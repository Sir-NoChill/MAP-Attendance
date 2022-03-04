package ui;

import exceptions.*;
import model.Employee;
import model.Role;
import model.State;
import model.WorkHours;
import model.leave.Leave;
import model.leave.LeaveType;
import persistance.JsonReader;
import persistance.JsonWriter;

import java.io.*;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Scanner;

import static model.Employee.stringToRole;
import static model.Employee.stringToWorkHours;

// Initial point of entry for the program
public class UI {
    private final Scanner scanner;
    private static final String JSON_STORE = "./data/programState.json";
    private State state;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    public UI() throws FileNotFoundException {
        this.scanner = new Scanner(System.in);
        this.state = new State(LocalDate.now());
        this.jsonReader = new JsonReader(JSON_STORE);
        this.jsonWriter = new JsonWriter(JSON_STORE);
        processOperations();
    }

    //REQUIRES:
    //MODIFIES: this
    //EFFECTS:  Entry point for user input in the console
    private void processOperations() {
        String operation;

        while (true) {
            System.out.println("Please type an operation or 'help' for a list of operations");
            operation = scanner.nextLine();
            System.out.println("You typed " + operation);

            if (operation.equals("quit")) {
                break;
            }

            String output = processOperation(operation);
            System.out.println(output);
        }
    }

    //REQUIRES: User input is typed correctly
    //MODIFIES: this
    //EFFECTS:  Runs one of the defined operations using user input from console
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private String processOperation(String operation) {
        switch (operation) {

            case "New Employee":
                String anniversary;
                Role role;
                String name;
                WorkHours workHours;
                String supervisor;
                String department;

                System.out.println("Please enter the employee Anniversary (yyyy-mm-dd)");
                anniversary = scanner.nextLine();

                System.out.println("Please enter the employee role (Human Resources, Accountant, or Legal Assistant)");
                String possibleRole = scanner.nextLine();
                try {
                    role = stringToRole(possibleRole);
                } catch (RoleNotFoundException e) {
                    System.out.println("Invalid role, we will set it to Legal assistant for now, "
                            + "you can change this later");
                    role = Role.LEGAL_ASSISTANT;
                }

                System.out.println("Please enter the employee name");
                name = scanner.nextLine();

                System.out.println("Please enter the number of hours the employee works per "
                        + "day (in a number, ex '7.5' or '7')");
                String workHoursPossible = scanner.nextLine();
                try {
                    workHours = stringToWorkHours(workHoursPossible);
                } catch (WorkHoursNotFoundException e) {
                    System.out.println("We did not understand that value, we will set it to 7, "
                            + "you can change this later");
                    workHours = WorkHours.SEVEN;
                }

                System.out.println("Please enter the employee's Supervisor");
                supervisor = scanner.nextLine();

                System.out.println("Please enter the employee's Department");
                department = scanner.nextLine();

                Employee employee = new Employee(anniversary,role,name,workHours,supervisor,department);
                state.addEmployee(employee);

                return "Created a new employee";

            case "Take Leave":
                String date = null;
                String potentialLeave;
                String comment;
                LeaveType leaveType = null;

                System.out.println("Please input the type of leave (Holiday or Sick)");
                potentialLeave = scanner.nextLine();

                try {
                    leaveType = stringToLeaveType(potentialLeave);
                } catch (InvalidLeaveTypeException e) {
                    System.out.println("That type is invalid, would you like to try again? Y/N");
                    String again = scanner.nextLine().toLowerCase(Locale.ROOT);
                    if (again.equals("y")) {
                        processOperation("Take Leave");
                    } else {
                        processOperations();
                    }
                }

                System.out.println("Please enter the date of leave ('yyyy-mm-dd')");
                date = scanner.nextLine();

                System.out.println("Please enter any comments");
                comment = scanner.nextLine();

                System.out.println("please enter the employee name");
                String employeeName = scanner.nextLine();
                try {
                    searchEmployee(employeeName).takeLeave(date,leaveType,comment);
                } catch (InvalidLeaveAmountException e) {
                    System.out.println("The employee does not have enough leave left to take more, override? Y/N");
                    String override = scanner.nextLine().toLowerCase(Locale.ROOT);
                    if (override.equals("y")) {
                        searchEmployee(employeeName).addLeaveToEmployee(date,leaveType,comment);
                    } else {
                        processOperations();
                    }
                }

                return employeeName + " took leave on " + date + ", comments:" + comment;

            case "Get Leave Taken":
                System.out.println("please enter the employee name who's leave you want to see");
                String name1 = scanner.nextLine();
                System.out.println(displayLeave(searchEmployee(name1)));

                return "Leave Taken";

            case "Get Employee Vacation Time":
                System.out.println("please enter the employee name who's remaining leave you want to see");
                String name2 = scanner.nextLine();
                System.out.println(searchEmployee(name2).getHolidayLeft());

                return "Vacation time";

            case "Get Employee Sick Leave Left":
                System.out.println("please enter the employee name who's remaining leave you want to see");
                String name3 = scanner.nextLine();
                System.out.println(searchEmployee(name3).getSickLeaveLeft());

                return "Sick leave left";

            case "Add/Change notes on leave taken":
                String name4;
                String date4 = null;
                String comment4;

                System.out.println("Please insert the name of the employee");
                name4 = scanner.nextLine();
                Employee employee1 = searchEmployee(name4);

                System.out.println("Please insert the date the leave was taken");
                date4 = scanner.nextLine();
                Leave leave4 = null;
                try {
                    leave4 = employee1.searchLeave(date4);
                } catch (LeaveNotFoundException e) {
                    System.out.println("We cannot find that leave");
                    processOperations();
                }

                System.out.println("Please enter the revised comments you would like");
                comment4 = scanner.nextLine();
                try {
                    leave4.setComments(comment4);
                } catch (NullPointerException e) {
                    leave4.setComments("");
                }

                return "comments changed";

            case "Change Department":
                String name5;
                String department5;

                System.out.println("Please insert the name of the employee");
                name5 = scanner.nextLine();
                Employee employee5 = searchEmployee(name5);

                System.out.println("please enter the new department name you would like for the employee");
                department5 = scanner.nextLine();
                employee5.setDepartment(department5);

                return "department updated";

            case "Change Supervisor":
                String name6;
                String supervisor6;

                System.out.println("Please insert the name of the employee");
                name6 = scanner.nextLine();
                Employee employee6 = searchEmployee(name6);

                System.out.println("please enter the new supervisor name you would like for the employee");
                supervisor6 = scanner.nextLine();
                employee6.setSupervisor(supervisor6);

                return "supervisor updated";

            case "End Day":
                this.state.incrementDate();

                return "day ended, the new date is " + this.state.getCurrentDate();

            case "Save":
                saveProgramState();

                return "";

            case "Load":
                loadProgramState();

                return "";

            case "help":
                help();

                return "";

            case "Quit":
                System.exit(0);
        }

        return "Operation Complete";
    }

    //REQUIRES: State.listOfEmployees be non-empty
    //MODIFIES:
    //EFFECTS:  returns an employee from the listOfEmployees with a matching name to the input
    public Employee searchEmployee(String employeeName) {
        for (Employee employee :
                state.getSetOfEmployees()) {
            if (employeeName.equals(employee.getName())) {
                return employee;
            }
        }
        return null;
    }

    //REQUIRES: leaveTaken be non-empty
    //EFFECTS:  Displays all leave taken by employee according to displayLeave
    public String displayLeave(Employee employee) {
        StringBuilder output = new StringBuilder();
        for (Leave leave :
                employee.getLeaveTaken()) {
            output.append(leave.displayLeave());
        }
        return output.toString();
    }

    //EFFECTS: saves a program state to a file
    private void saveProgramState() {
        try {
            jsonWriter.open();
            jsonWriter.write(state);
            jsonWriter.close();
            System.out.println("Saved state at " + LocalDate.now() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    //EFFECTS: attempts to read a program state from file
    private void loadProgramState() {
        try {
            state = jsonReader.read();
            System.out.println("Loaded state from file successfully");
        } catch (IOException e) {
            System.out.println("Unable to read file at: " + JSON_STORE);
        }
    }

    //EFFECTS: terminal output of a help message
    private void help() {
        String message = "New Employee - create a new employee\n"
                + "Take Leave - make an employee take leave on a given day\n"
                + "Get Leave Taken - show all leave taken by a particular employee\n"
                + "Get Employee Vacation Time - see how much vacation an employee has left\n"
                + "Get Employee Sick Leave Left - see how much sick leave an employee has left\n"
                + "Add/Change notes on leave taken - Change the note on a day of leave taken by the employee\n"
                + "Change Department - Change an employee's Department\n"
                + "Change Supervisor - Change an employee's Supervisor\n"
                + "End Day - Change the day to one day in the future\n"
                + "Save - Saves the current state of the program to a file named " + JSON_STORE + "\n"
                + "Load - Loads a state from the directory: " + JSON_STORE + "\n"
                + "Quit - exit the program";
        System.out.println(message);
    }


    //Effects: converts a string of characters to a leaveType
    private LeaveType stringToLeaveType(String potentialLeave) throws InvalidLeaveTypeException {
        LeaveType l;
        switch (potentialLeave) {
            case "Holiday":
                l = LeaveType.HOLIDAY;
                break;

            case "Sick":
                l = LeaveType.SICK;
                break;

            default:
                throw new InvalidLeaveTypeException();
        }
        return l;
    }
}
