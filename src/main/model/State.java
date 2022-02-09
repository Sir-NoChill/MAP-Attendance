package model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.time.LocalDate.parse;

public class State {
    LocalDate currentDate;
    Set<Employee> listOfEmployees;

    //Object created at the start of the program, inherits last state plus any changes that should
    // have occured between the date of last opening and the present
    public State(LocalDate initialDate) {
        this.currentDate = initialDate;
        this.listOfEmployees = new HashSet<Employee>();
    }

    public State(String initialDate ) {
        this.currentDate = parse(initialDate);
    }

    //TODO check if any accruals need to happen
    // Runs if the first day of the month
    //REQUIRES: Change of Day
    //MODIFIES: this
    //EFFECTS:  Signals the temporal state of the program to advance
    public void updateState(int days) { // keep your cardiologists number on speed dial

    }

    //REQUIRES:
    //MODIFIES: this
    //EFFECTS:  Adds an employee to the list of employees
    public void addEmployee(Employee employee) {

    }

    //REQUIRES: employees must be non-empty
    //MODIFIES:
    //EFFECTS : prints the name of all employees
    public String displayEmployees() {//FIXME this should be changed to a more proper representation of a table etc.

        return null;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public Set<Employee> getListOfEmployees() {
        return listOfEmployees;
    }
}
