package ui;

import model.Employee;
import model.Role;
import model.State;
import model.WorkHours;
import model.leave.Leave;
import model.leave.LeaveType;

import java.time.LocalDate;
import java.util.Scanner;

public class UI {
    private final Scanner scanner;
    private State state;

    public UI() {
        this.scanner = new Scanner(System.in);
        this.state = new State(LocalDate.now());
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
            System.out.println("You have " + output);
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
                String possibleRole = scanner.nextLine(); //TODO may need to implement a while loop in case the user
                                                          // types wrong
                switch (possibleRole) {

                    case "Human Resources":
                        role = Role.HUMAN_RESOURCES;
                        break;

                    case "Accountant":
                        role = Role.ACCOUNTANT;
                        break;

                    case "Legal Assistant":
                        role = Role.LEGAL_ASSISTANT;
                        break;

                    default:
                        role = Role.LEGAL_ASSISTANT;
                        break;
                }

                System.out.println("Please enter the employee name");
                name = scanner.nextLine();

                System.out.println("Please enter the number of hours the employee works per day (in a number, ex '7')");
                String workHoursPossible = scanner.nextLine();
                switch (workHoursPossible) {

                    case "7.5":
                        workHours = WorkHours.SEVEN_HALF;
                        break;

                    case "6.5":
                        workHours = WorkHours.SIX_HALF;
                        break;

                    case "7":
                        workHours = WorkHours.SEVEN;
                        break;

                    default:
                        workHours = WorkHours.SEVEN;
                        break;
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
                LeaveType leaveType;

                System.out.println("Please input the type of leave (Vacation or Sick)");
                potentialLeave = scanner.nextLine();

                switch (potentialLeave) {
                    case "Vacation":
                        leaveType = LeaveType.HOLIDAY;
                        break;

                    case "Sick":
                        leaveType = LeaveType.SICK;
                        break;

                    default:
                        System.out.println("the type you entered has no match");
                        //While loop?
                        leaveType = LeaveType.SICK;
                        break;
                }

                System.out.println("Please enter the date of leave ('yyyy-mm-dd')");
                date = scanner.nextLine();

                System.out.println("Please enter any comments");
                comment = scanner.nextLine();

                System.out.println("please enter the employee name");
                String employeeName = scanner.nextLine();
                searchEmployee(employeeName).takeLeave(date,leaveType,comment);

                return employeeName + " took leave on " + date + ", comments:" + comment;

            case "Get Leave Taken":
                System.out.println("please enter the employee name who's leave you want to see");
                String name1 = scanner.nextLine();
                System.out.println(searchEmployee(name1).displayLeave());

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
                Leave leave4 = employee1.searchLeave(date4);

                System.out.println("Please enter the revised comments you would like");
                comment4 = scanner.nextLine();
                leave4.setComments(comment4);

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

            case "help":
                System.out.println();
        }

        return "Operation Complete";
    }

    //REQUIRES: State.listOfEmployees be non-empty
    //MODIFIES:
    //EFFECTS:  returns an employee from the listOfEmployees with a matching name to the input
    public Employee searchEmployee(String employeeName) {
        for (Employee employee :
                state.getListOfEmployees()) {
            if (employeeName.equals(employee.getName())) {
                return employee;
            }
        }
        return null;
    }

    //EFFECTS: Entry point for console application
    public static void main(String[] args) {
        new UI();
    }
}
