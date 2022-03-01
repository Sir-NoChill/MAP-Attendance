package persistance;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

import model.Employee;
import model.Role;
import model.State;
import model.WorkHours;
import model.leave.Holiday;
import model.leave.Leave;
import model.leave.LeaveType;
import model.leave.Sick;
import org.json.*;

import static model.Role.ACCOUNTANT;

// Modeled after JsonSerializationDemo CPSC 210 2022W
// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    public State read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseState(jsonObject);
    }

    public String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    private State parseState(JSONObject jsonObject) {
        String date = jsonObject.getString("currentDate");
        State s = new State(date);
        addEmployees(s, jsonObject);
        return s;
    }

    private void addEmployees(State state, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("employees");
        for (Object json : jsonArray) {
            JSONObject nextEmployee = (JSONObject) json;
            addEmployee(state, nextEmployee);
        }
    }

    private void addEmployee(State state, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String supervisor = jsonObject.getString("supervisor");
        String department = jsonObject.getString("department");

        String role = jsonObject.getString("role");
        String workHours = jsonObject.getString("workHours");

        String anniversary = jsonObject.getString("anniversary");

        double holidayLeft = jsonObject.getDouble("holidayLeft");

        double sickLeaveLeft = jsonObject.getDouble("sickLeaveLeft");

        Role role1 = Employee.stringToRole(role.toLowerCase());
        WorkHours workHours1 = Employee.stringToWorkHours(workHours.toLowerCase());

        Employee e = new Employee(anniversary, role1, name, workHours1, supervisor, department);
        e.setHolidayLeft(holidayLeft);
        e.setSickLeaveLeft(sickLeaveLeft);
        addLeaves(e, jsonObject);
        state.addEmployee(e);
    }

    private void addLeaves(Employee employee, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("leave");
        for (Object json : jsonArray) {
            JSONObject nextLeave = (JSONObject) json;
            addLeave(employee,nextLeave);
        }
    }

    private void addLeave(Employee employee, JSONObject jsonObject) {
        String date = jsonObject.getString("leaveDate");
        String comments = jsonObject.getString("comments");

        if (Objects.equals(jsonObject.getString("leaveType"), "Sick")) {
            employee.takeLeave(date, LeaveType.SICK,comments);
        } else {
            employee.takeLeave(date, LeaveType.HOLIDAY,comments);//TODO make sure to add these into the toJson in leave
        }

    }
}
