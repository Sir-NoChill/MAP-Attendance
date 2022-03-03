package model;

import model.leave.Leave;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

// Represents a state of the program at a given date
import static java.time.LocalDate.parse;

public class State {
    private LocalDate currentDate;
    private Set<Employee> listOfEmployees;


    //Object created at the start of the program, inherits last state plus any changes that should
    // have occured between the date of last opening and the present
    public State(LocalDate initialDate) {
        this.currentDate = initialDate;
        this.listOfEmployees = new HashSet<>();
    }

    public State(String initialDate) {
        this.currentDate = parse(initialDate);
        this.listOfEmployees = new HashSet<>();
    }

    //REQUIRES:
    //MODIFIES: this
    //EFFECTS:  Adds an employee to the list of employees
    public void addEmployee(Employee employee) {
        this.listOfEmployees.add(employee);
    }

    //REQUIRES: employees must be non-empty
    //MODIFIES:
    //EFFECTS : prints the name of all employees
    public String displayEmployees() { //FIXME this should be changed to a more proper representation of a table etc.
        StringBuilder names = new StringBuilder();
        for (Employee employee : listOfEmployees) {
            names.append(employee.getName()).append(", ");
        }

        return names.toString();
    }

    //EFFECTS:  Returns an instance of Employee with matching name.
    public Employee searchEmployees(String name) {
        for (Employee e: this.listOfEmployees) {
            if (e.getName().equals(name)) {
                return e;
            }
        }
        return null;
    }

    //REQUIRES: initial state exist
    //          and be distinct from attempted update
    //          (//FIXME) and in the future (to change)
    //MODIFIES: this
    //EFFECTS: changes the current date of the state
    //         updates employees holiday
    //         updates employee sick
    //TODO Find a way to restore a previous state
    //TODO Implement in the save system
    public void update(LocalDate newDate) {
        long daysBetween = ChronoUnit.DAYS.between(this.currentDate,newDate);
        int intDaysBetween = Math.toIntExact(daysBetween);
        for (int n = 0; n < intDaysBetween; n++) {
            incrementDate();
        }
        this.currentDate = newDate;
    }

    // MODIFIES: this
    // EFFECTS:  Increments the day by 1
    public void incrementDate() {
        LocalDate date = currentDate.plusDays(1); //This needs to be awkward since the LocalDate is immutable
        this.currentDate = date;
        this.updateEmployees();
    }


    //REQUIRES: listOfEmployees is non-empty
    //MODIFIES: employees in listOfEmployees
    //EFFECTS:  updates the employees in the list of employees in the state
    public void updateEmployees() {
        for (Employee employee : listOfEmployees) {
            if ((currentDate.getMonth() == employee.getAnniversary().getMonth())
                    && (currentDate.getDayOfMonth() == employee.getAnniversary().getDayOfMonth())) {
                employee.accrueHoliday();
            }
            if ((currentDate.getMonthValue() == 1) && (currentDate.getDayOfMonth() == 1)) {
                employee.accrueSickDays();
            }
        }
    }

    // EFFECTS: converts a state to a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("currentDate",currentDate.toString());
        json.put("employees",employeesToJsonArray());

        return json;
    }

    // EFFECTS: converts all the employees in a state into a JSON Array
    private JSONArray employeesToJsonArray() {
        JSONArray json = new JSONArray();

        for (Employee e : listOfEmployees) {
            json.put(e.toJson());
        }

        return json;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public Set<Employee> getSetOfEmployees() {
        return listOfEmployees;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }
}
