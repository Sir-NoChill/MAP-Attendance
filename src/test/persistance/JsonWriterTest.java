package persistance;

import jdk.nashorn.internal.ir.debug.JSONWriter;
import model.Employee;
import model.Role;
import model.State;
import model.WorkHours;
import model.leave.LeaveType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            State s = new State("2022-01-01");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:filename.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyState() {
        try {
            State s = new State("2022-01-01");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyState.json");
            writer.open();
            writer.write(s);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyState.json");
            s = reader.read();
            assertEquals(LocalDate.parse("2022-01-01"), s.getCurrentDate());
            assertEquals(0, s.getSetOfEmployees().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralState() {
        JsonWriter writer = new JsonWriter("./data/testWriterGeneralState.json");
        State s = new State("2022-01-01");
        Employee employee = new Employee(LocalDate.parse("2012-01-01"),
                Role.HUMAN_RESOURCES,"Jerry", WorkHours.SEVEN,"Mom",
                "Harry Potter");
        employee.setHolidayLeft(50);
        employee.setSickLeaveLeft(50);
        employee.addLeaveToEmployee("2021-03-04", LeaveType.SICK,"Hit by a bus");
        s.addEmployee(employee);
        employee.addLeaveToEmployee("2020-01-04",LeaveType.HOLIDAY,"Wedding");

        try {
            writer.open();
            writer.write(s);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralState.json");
            s = reader.read();
            assertEquals(LocalDate.parse("2022-01-01"),s.getCurrentDate());
            assertEquals(1,s.getSetOfEmployees().size());
            Employee employee1 = s.searchEmployees("Jerry");
            assertEquals("Jerry",employee1.getName());
            assertEquals(2,employee1.getLeaveTaken().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

}
