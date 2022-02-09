package tests;

import model.Employee;
import model.State;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

import static model.Role.*;
import static model.Role.ACCOUNTANT;
import static model.WorkHours.*;
import static model.WorkHours.SIX_HALF;

public class StateTest {

    State initialState;
    State finalState;
    State currentState;

    Employee testEmployee1;
    Employee testEmployee2;
    Employee testEmployee3;
    Employee testEmployee4;

    //@BeforeAll
    //public static void setupEmployees() {
    //}//FIXME how does this @beforeall tag work

    @BeforeEach
    public void setup() {
        initialState = new State("2022-02-09");
        finalState = new State("2100-01-01");
        currentState = new State(LocalDate.now());
        testEmployee1 = new Employee("2002-07-08", LEGAL_ASSISTANT, "Jerry", SIX_HALF, "Jane", "Criminal Law");
        testEmployee2 = new Employee(LocalDate.now(), HUMAN_RESOURCES, "Harold", SEVEN_HALF, "Kane", "Realty");
        testEmployee3 = new Employee("2015-08-07",ACCOUNTANT,"Merry",SEVEN,"Bane","Wills");
        testEmployee4 = new Employee(LocalDate.of(2017,5,13),ACCOUNTANT,"Carol",SIX_HALF,"Vane","Bert");
    }

    @Test
    public void testStateConstructor() {
        assertEquals(LocalDate.parse("2022-02-09"),initialState.getCurrentDate());
        assertTrue(initialState.getListOfEmployees().isEmpty());

        assertEquals(LocalDate.parse("2100-01-01"),finalState.getCurrentDate());
        assertTrue(finalState.getListOfEmployees().isEmpty());

        assertEquals(LocalDate.now(),currentState.getCurrentDate());
        assertTrue(currentState.getListOfEmployees().isEmpty());
    }

    @Test
    public void testUpdateState() {
        finalState.updateState(1);
        assertEquals(LocalDate.parse("2100-01-02"),finalState.getCurrentDate());
        finalState.updateState(1);
        assertEquals(LocalDate.parse("2100-01-03"),finalState.getCurrentDate());
    }

    @Test
    public void testAddEmployee() {
        initialState.addEmployee(testEmployee1);
        assertEquals(1,initialState.getListOfEmployees().size());
        initialState.addEmployee(testEmployee2);
        assertEquals(2,initialState.getListOfEmployees().size());
        initialState.addEmployee(testEmployee2);
        assertEquals(2,initialState.getListOfEmployees().size());
    }

    @Test
    public void testDisplayEmployee() {
        initialState.addEmployee(testEmployee2);
        initialState.addEmployee(testEmployee1);
        assertEquals("Harold, Jerry",initialState.displayEmployees());
    }


}
