package persistance;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

import exceptions.FileLoadError;
import exceptions.RoleNotFoundException;
import model.*;
import model.leave.LeaveType;
import org.json.*;

// Modeled after JsonSerializationDemo CPSC 210 2022W
// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    //EFFECTS: Reads a state from a JSON file
    public State read() throws IOException, FileLoadError {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        State s = parseState(jsonObject);
        Event e = new Event("State loaded from: " + this.source);
        EventLog.getInstance().logEvent(e);
        return s;
    }

    //EFFECTS: converts a JSON file into a Java Readable String
    public String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    //EFFECTS: parses a JSON file into a state
    private State parseState(JSONObject jsonObject) throws FileLoadError {
        String date = jsonObject.getString("currentDate");
        State s = new State(date);
        addEmployees(s, jsonObject);
        return s;
    }

    //EFFECTS: Adds all employees from JSON Array to a state when loaded form a file
    private void addEmployees(State state, JSONObject jsonObject) throws FileLoadError {
        JSONArray jsonArray = jsonObject.getJSONArray("employees");
        for (Object json : jsonArray) {
            JSONObject nextEmployee = (JSONObject) json;
            addEmployee(state, nextEmployee);
        }
    }

    //EFFECTS: Adds an employee to a state when loaded from JSON file
    private void addEmployee(State state, JSONObject jsonObject) throws FileLoadError {
        String name = jsonObject.getString("name");
        String supervisor = jsonObject.getString("supervisor");
        String department = jsonObject.getString("department");

        String role = jsonObject.getString("role");

        String anniversary = jsonObject.getString("anniversary");

        double holidayLeft = jsonObject.getDouble("holidayLeft");

        double sickLeaveLeft = jsonObject.getDouble("sickLeaveLeft");

        double workHours = jsonObject.getDouble("workHours");

        Role role1;
        try {
            role1 = Employee.stringToRole(role.toLowerCase());
        } catch (RoleNotFoundException e) {
            throw new FileLoadError();
        }

        Employee e = new Employee(anniversary, role1, name, workHours, supervisor, department);
        e.setHolidayLeft(holidayLeft);
        e.setSickLeaveLeft(sickLeaveLeft);
        addLeaves(e, jsonObject);
        state.addEmployee(e);
    }

    //EFFECTS: adds all the leave from a JSON Array to an employee when loading a file
    private void addLeaves(Employee employee, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("leave");
        for (Object json : jsonArray) {
            JSONObject nextLeave = (JSONObject) json;
            addLeave(employee,nextLeave);
        }
    }

    //EFFECTS: adds leave to an employee from the read file
    private void addLeave(Employee employee, JSONObject jsonObject) {
        String date = jsonObject.getString("leaveDate");
        String comments = jsonObject.getString("comments");
        double timeSegments = jsonObject.getDouble("timeSegments");

        if (Objects.equals(jsonObject.getString("leaveType"), "Sick")) {
            employee.addLeaveToEmployee(date, LeaveType.SICK,comments,timeSegments);
        } else {
            employee.addLeaveToEmployee(date, LeaveType.HOLIDAY,comments,timeSegments);
        }

    }
}
