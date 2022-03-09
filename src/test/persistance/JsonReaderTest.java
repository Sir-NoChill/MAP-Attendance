package persistance;

import model.Employee;
import model.Role;
import model.State;
import model.WorkHours;
import model.leave.LeaveType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest extends JsonTest{

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            State s = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyState() {
        JsonReader reader = new JsonReader("./data/emptyState.json");
        try {
            State s = reader.read();
            assertEquals(LocalDate.parse("2022-03-01"),s.getCurrentDate());
            assertTrue(s.getSetOfEmployees().isEmpty());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderOneEmployeeState() {
        JsonReader reader = new JsonReader("./data/oneEmployeeState.json");
        try {
            State s = reader.read();
            Employee employee = new Employee(LocalDate.parse("2012-01-01"),
                    Role.HUMAN_RESOURCES,"Jerry", 7,"Mom",
                    "Harry Potter");
            employee.addLeaveToEmployee("2018-07-13", LeaveType.SICK,"Jerry got eaten by a possum",28);
            assertEquals(LocalDate.parse("2022-03-01"),s.getCurrentDate());
            assertEquals(1,s.getSetOfEmployees().size());
        } catch (IOException e) {
            fail("Could not read from file");
        }
    }
}
