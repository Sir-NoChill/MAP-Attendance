package persistance;

import model.Employee;
import model.Role;
import model.State;
import model.WorkHours;
import model.leave.Holiday;
import model.leave.Sick;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class JsonTest {
    protected void checkEmployee(LocalDate anniversary, Role role, String name, WorkHours workHours,
                                 String supervisor, String department, Employee employee) {
        assertEquals(anniversary, employee.getAnniversary());
        assertEquals(role,employee.getRole());
        assertEquals(name,employee.getName());
        assertEquals(workHours,employee.getWorkHours());
        assertEquals(supervisor,employee.getSupervisor());
        assertEquals(department,employee.getDepartment());
    }

    protected void checkHoliday(LocalDate dateOfLeave, String comments, Holiday holiday) {
        assertEquals(dateOfLeave,holiday.getDateOfLeave());
        assertEquals(comments,holiday.getComments());
    }

    protected void checkSick(LocalDate dateOfLeave, String comments, Sick holiday) {
        assertEquals(dateOfLeave,holiday.getDateOfLeave());
        assertEquals(comments,holiday.getComments());
    }

    protected void checkState(LocalDate date, Set<Employee> employees, State state) {
        for (Employee employee : state.getSetOfEmployees()) {
            assertTrue(employees.contains(employee));
        }
        assertEquals(date,state.getCurrentDate());
    }
}
