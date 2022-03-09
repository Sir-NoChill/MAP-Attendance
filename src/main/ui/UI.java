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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Scanner;

import static model.Employee.stringToRole;
import static model.Employee.stringToWorkHours;

public class UI {
    private Scanner scanner;
    private static final String JSON_STORE = "./data/programState.json";
    private State state;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    public UI() throws FileNotFoundException {
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
        scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Please type an operation or 'help' for a list of operations");
            operation = scanner.nextLine();
            System.out.println("You typed " + operation);
            processOperation(operation);

            if (operation.toLowerCase(Locale.ROOT).equals("quit")) {
                break;
            }

            System.out.println("Operation Complete");
        }
    }

    //REQUIRES: User input is typed correctly
    //MODIFIES: this
    //EFFECTS:  Runs one of the defined operations using user input from console
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void processOperation(String operation) {
        operation = operation.toLowerCase(Locale.ROOT);
        switch (operation) {
            case "new employee":
                createNewEmployeeUI();
                break;
            case "take sick leave":
                employeeTakeSickUI();
                break;
            case "take holiday":
                employeeTakeHolidayUI();
                break;
            case "get leave taken":
                displayLeaveUI();
                break;
            case "get employee vacation time":
                checkEmployeeHolidayLeave();
                break;
            case "get employee sick leave left":
                checkEmployeeSickLeave();
                break;
            case "add/change notes on leave taken":
                changeNotesOnLeave();
                break;
            case "change department":
                changeDepartment();
                break;
            case "change supervisor":
                changeSupervisor();
                break;
            case "end day":
                this.state.incrementDate();
                break;
            case "save":
                saveProgramState();
                break;
            case "help":
                help();
                break;
            case "load":
                loadProgramState();
                break;
            case "change employee role":
                changeRoleUI();
                break;
            case "change employee work hours":
                changeWorkHoursUI();
                break;
            case "display employees":
                displayEmployeesUI();
                break;
            case "display employee leave":
                displayEmployeeLeaveUI();
                break;
            case "show current date":
                showCurrentDateUI();
                break;
        }
    }

    //EFFECTS: shows the current date
    private void showCurrentDateUI() {
        System.out.println(state.getCurrentDate());
    }

    //EFFECTS: display all instances of leave for an employee
    private static void displayEmployeeLeaveInEmployee(Employee employee) {
        StringBuilder message = new StringBuilder();
        for (Leave l :
                employee.getLeaveTaken()) {
            message.append(l.getDateOfLeave()).append("\n");
            message.append(l.getLeaveType()).append("\n");
            message.append(l.getComments());

            System.out.println(message);
        }
    }

    //EFFECTS: show all instances of leave for a given employee
    private void displayEmployeeLeaveUI() {
        boolean bool = true;
        Employee e;

        while (bool) {
            try {
                System.out.println("please type the name of the employee who's leave you would like to see");
                e = state.searchEmployees(scanner.nextLine());

                displayEmployeeLeaveInEmployee(e);
            } catch (EmployeeNotFoundException ex) {
                System.out.println("We couldn't find that employee, care to try again? Y/N");
                if (scanner.nextLine().toLowerCase(Locale.ROOT).equals("n")) {
                    bool = false;
                }
            }
        }
    }

    //EFFECTS: displays all employees in the current state
    private void displayEmployeesUI() {
        for (Employee e :
                state.getSetOfEmployees()) {
            System.out.println(e.getName() + "\n");
        }
    }

    //MODIFIES this
    //EFFECTS: changes the work hours of an employee in the current state
    private void changeWorkHoursUI() {
        Employee e;
        boolean bool = true;
        while (bool) {
            try {
                System.out.println("Please type the name of the employee who's work hours you would like to change");
                e = state.searchEmployees(scanner.nextLine());

                System.out.println("Please type their new number of hours per day");
                double workHours = scanner.nextDouble();
                e.setWorkHours(workHours);
                bool = false;
            } catch (EmployeeNotFoundException ex) {
                System.out.println("We couldn't find that employee, care to try again? Y/N");
                if (scanner.nextLine().toLowerCase(Locale.ROOT).equals("n")) {
                    bool = false;
                }
            }
        }
    }


    // MODIFIES: this
    // EFFECTS: changes the role of an employee in the current state
    private void changeRoleUI() {
        Employee e;
        boolean bool = true;
        while (bool) {
            try {
                System.out.println("Please type the name of the employee who's role you would like to change");
                e = state.searchEmployees(scanner.nextLine());

                System.out.println("Please type their new role (hr, accountant, legal assistant)");
                Role role = stringToRole(scanner.nextLine());
                e.setRole(role);
                bool = false;
            } catch (EmployeeNotFoundException ex) {
                System.out.println("We couldn't find that employee, care to try again? Y/N");
                if (scanner.nextLine().toLowerCase(Locale.ROOT).equals("n")) {
                    bool = false;
                }
            } catch (RoleNotFoundException ex) {
                System.out.println("That role type doesn't exist, care to try again? Y/N");
                if (scanner.nextLine().toLowerCase(Locale.ROOT).equals("n")) {
                    bool = false;
                }
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: creates a new employee with user input
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void createNewEmployeeUI() {
        scanner = new Scanner(System.in);
        boolean bool = true;
        Role role = null;
        double workHours = 0;

        while (bool) {
            System.out.println("Please type the following followed by a newline: anniversary (yyyy-mm-dd),"
                    + "role (HR, Accountant, Legal Assistant), name, number of hours worked per day"
                    + "(6.5,7,7.5), supervisor and department. If something does not work we will tell you.");
            String anniversary = scanner.nextLine();
            try {
                role = stringToRole(scanner.nextLine());
            } catch (RoleNotFoundException e) {
                System.out.println("We did not get that role, try again? Y/N");
                if (scanner.nextLine().toLowerCase(Locale.ROOT).equals("n")) {
                    bool = false;
                }
            }
            String name = scanner.nextLine();
            workHours = scanner.nextDouble();


            String supervisor = scanner.nextLine();
            String department = scanner.nextLine();

            Employee employee = new Employee(anniversary,role,name,workHours,supervisor,department);
            state.addEmployee(employee);
            System.out.println("Finished");
            bool = false;
        }
    }

    //MODIFIES: this
    //EFFECTS: adds an instance of Sick to Employee in State
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void employeeTakeSickUI() {
        scanner = new Scanner(System.in);
        boolean bool = true;
        Employee employee = null;
        String date = null;
        String comments = null;
        double time = 0;

        while (bool) {
            try {
                System.out.println("Please type the name of the employee who will take leave");
                employee = state.searchEmployees(scanner.nextLine());

                System.out.println("Please type the date the leave will be taken (yyyy-mm-dd)");
                date = scanner.nextLine();

                System.out.println("Please type any comments:");
                comments = scanner.nextLine();

                System.out.println("Please type the number of time segments the employee will take off"
                        + " (15-minute increments)");
                time = scanner.nextDouble();

                employee.takeLeave(date, LeaveType.SICK,comments,time);
                bool = false;
            } catch (EmployeeNotFoundException e) {
                System.out.println("We couldn't find that employee, try again? Y/N");
                if (scanner.nextLine().toLowerCase(Locale.ROOT).equals("n")) {
                    bool = false;
                }
            } catch (InvalidLeaveAmountException e) {
                System.out.println("The employee has no more leave of that type left, override? Y/N");
                if (scanner.nextLine().toLowerCase(Locale.ROOT).equals("y")) {
                    employee.addLeaveToEmployee(date, LeaveType.SICK, comments,time);
                }
            }
        }
    }


    //MODIFIES: this
    //EFFECTS: adds an instance of Holiday to Employee in State
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void employeeTakeHolidayUI() {
        scanner = new Scanner(System.in);
        boolean bool = true;
        Employee employee = null;
        String date = null;
        String comments = null;
        double time = 0;

        while (bool) {
            try {
                System.out.println("Please type the name of the employee who will take leave");
                employee = state.searchEmployees(scanner.nextLine());

                System.out.println("Please type the date the leave will be taken (yyyy-mm-dd)");
                date = scanner.nextLine();

                System.out.println("Please type any comments:");
                comments = scanner.nextLine();

                System.out.println("Please type the number of time segments the employee will take off"
                        + " (15-minute increments)");
                time = scanner.nextDouble();

                employee.takeLeave(date, LeaveType.HOLIDAY,comments,time);
                bool = false;
            } catch (EmployeeNotFoundException e) {
                System.out.println("We couldn't find that employee, try again? Y/N");
                if (scanner.nextLine().toLowerCase(Locale.ROOT).equals("n")) {
                    bool = false;
                }
            } catch (InvalidLeaveAmountException e) {
                System.out.println("The employee has no more leave of that type left, override? Y/N");
                if (scanner.nextLine().toLowerCase(Locale.ROOT).equals("y")) {
                    employee.addLeaveToEmployee(date, LeaveType.HOLIDAY, comments,time);
                }
            }
        }
    }

    //EFFECTS: displays the employee's leave taken
    private void displayLeaveUI() {
        scanner = new Scanner(System.in);
        boolean bool = true;

        while (bool) {
            try {
                System.out.println("Please type the name of the employee who's leave you would like to see");
                Employee e = state.searchEmployees(scanner.nextLine());
                System.out.println(displayLeave(e));
                bool = false;
            } catch (EmployeeNotFoundException e) {
                System.out.println("We couldn't find that employee, try again? Y/N");
                if (scanner.nextLine().toLowerCase(Locale.ROOT).equals("n")) {
                    bool = false;
                }
            }
        }
    }

    //EFFECTS: returns the employee holiday leave remaining
    private void checkEmployeeHolidayLeave() {
        scanner = new Scanner(System.in);
        boolean bool = true;

        while (bool) {
            try {
                System.out.println("Please type the name of the employee who's leave you would like to get");
                Employee e = state.searchEmployees(scanner.nextLine());
                System.out.println(e.getHolidayLeft());
                bool = false;
            } catch (EmployeeNotFoundException e) {
                System.out.println("We couldn't find that employee, try again? Y/N");
                if (scanner.nextLine().toLowerCase(Locale.ROOT).equals("n")) {
                    bool = false;
                }
            }
        }
    }

    //EFFECTS: returns the employee sick leave remaining
    private void checkEmployeeSickLeave() {
        scanner = new Scanner(System.in);
        boolean bool = true;

        while (bool) {
            try {
                System.out.println("Please type the name of the employee who's leave you would like to get");
                Employee e = state.searchEmployees(scanner.nextLine());
                System.out.println(e.getSickLeaveLeft());
                bool = false;
            } catch (EmployeeNotFoundException e) {
                System.out.println("We couldn't find that employee, try again? Y/N");
                if (scanner.nextLine().toLowerCase(Locale.ROOT).equals("n")) {
                    bool = false;
                }
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: changes the comments on an instance of leave attached to an employee
    private void changeNotesOnLeave() {
        scanner = new Scanner(System.in);
        boolean bool = true;
        String again;

        while (bool) {
            try {
                System.out.println("Please type the name of the employee who's leave you would like to change");
                Employee e = state.searchEmployees(scanner.nextLine());

                System.out.println("Please type the date of leave");
                Leave l = e.searchLeave(scanner.nextLine());

                System.out.println("Please type the new comments");
                l.setComments(scanner.nextLine());

                bool = false;
            } catch (EmployeeNotFoundException | LeaveNotFoundException e) {
                System.out.println("That didn't work, try again? Y/N");
                again = scanner.nextLine();
                if (again.equals("n")) {
                    bool = false;
                }
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: changes the department of an employee
    private void changeDepartment() {
        scanner = new Scanner(System.in);
        boolean bool = true;

        while (bool) {
            System.out.println("Please type the name of the employee who's department you would like to change");
            try {
                Employee e = state.searchEmployees(scanner.nextLine());
                System.out.println("Please type the name of the new department");
                e.setSupervisor(scanner.nextLine());
                System.out.println("thank you");
                bool = false;

            } catch (EmployeeNotFoundException e) {
                System.out.println("we did not find that employee, would you like to try again? Y/N");
                String again = scanner.nextLine().toLowerCase(Locale.ROOT);
                if (again.equals("n")) {
                    bool = false;
                }
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: changes the supervisor of an employee
    private void changeSupervisor() {
        scanner = new Scanner(System.in);
        boolean bool = true;

        while (bool) {
            System.out.println("Please type the name of the employee who's supervisor you would like to change");
            try {
                Employee e = state.searchEmployees(scanner.nextLine());
                System.out.println("Please type the name of the new supervisor");
                e.setSupervisor(scanner.nextLine());
                System.out.println("thank you");
                bool = false;

            } catch (EmployeeNotFoundException e) {
                System.out.println("we did not find that employee, would you like to try again? Y/N");
                String again = scanner.nextLine().toLowerCase(Locale.ROOT);
                if (again.equals("n")) {
                    bool = false;
                }
            }
        }
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
            if (!state.getCurrentDate().equals(LocalDate.now())) {
                System.out.println("Would you like to update the date from "
                        + state.getCurrentDate().toString() + " to "
                        + LocalDate.now() + " Y/N");
                if (scanner.nextLine().toLowerCase(Locale.ROOT).equals("y")) {
                    state.update(LocalDate.now());
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to read file at: " + JSON_STORE);
        }
    }

    //EFFECTS: terminal output of a help message
    private void help() {
        String message = "New Employee - create a new employee\n"
                + "Change Employee Role - changes the role of an employee\n"
                + "Change Employee Work Hours - changes the work hours of an employee"
                + "Take Holiday - make an employee take holiday on a given day\n"
                + "Take Sick Leave - make an employee take a sick day on a given day\n"
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
