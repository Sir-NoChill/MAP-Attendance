package model;

import exceptions.EmployeeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import static model.Role.*;
import static model.Role.ACCOUNTANT;

public class StateTest {

    State initialState;
    State finalState;
    State currentState;
    State employeeState;

    Employee testEmployee1;
    Employee testEmployee2;
    Employee testEmployee3;
    Employee testEmployee4;

    Employee easyTestEmployee1;
    Employee easyTestEmployee2;
    Employee easyTestEmployee3;

    @BeforeEach
    public void setup() {
        initialState = new State("2022-02-09");
        finalState = new State("2100-01-01");
        currentState = new State(LocalDate.now());

        testEmployee1 = new Employee("2002-07-08", LEGAL_ASSISTANT, "Jerry", 6.5, "Jane", "Criminal Law");
        testEmployee2 = new Employee(LocalDate.now(), HUMAN_RESOURCES, "Harold", 7.5, "Kane", "Realty");
        testEmployee3 = new Employee("2015-08-07",ACCOUNTANT,"Merry",7,"Bane","Wills");
        testEmployee4 = new Employee(LocalDate.of(2017,5,13),ACCOUNTANT,"Carol",6.5,"Vane","Bert");

        easyTestEmployee1 = new Employee("2002-01-01", LEGAL_ASSISTANT, "Jerry", 6.5, "Jane", "Criminal Law");
        easyTestEmployee2 = new Employee("2002-07-08", LEGAL_ASSISTANT, "Jerry", 6.5, "Jane", "Criminal Law");
        easyTestEmployee3 = new Employee("2002-07-08", LEGAL_ASSISTANT, "Jerry", 6.5, "Jane", "Criminal Law");

        employeeState = new State("2010-01-01");
    }

    @Test
    public void testStateConstructor() {
        assertEquals(LocalDate.parse("2022-02-09"),initialState.getCurrentDate());
        assertTrue(initialState.getSetOfEmployees().isEmpty());

        assertEquals(LocalDate.parse("2100-01-01"),finalState.getCurrentDate());
        assertTrue(finalState.getSetOfEmployees().isEmpty());

        assertEquals(LocalDate.now(),currentState.getCurrentDate());
        assertTrue(currentState.getSetOfEmployees().isEmpty());
    }

    @Test
    public void testAddEmployee() {
        initialState.addEmployee(testEmployee1);
        assertEquals(1,initialState.getSetOfEmployees().size());
        initialState.addEmployee(testEmployee2);
        assertEquals(2,initialState.getSetOfEmployees().size());
        initialState.addEmployee(testEmployee2);
        assertEquals(2,initialState.getSetOfEmployees().size());
    }

    @Test
    public void testUpdateEmployeesCase1() { //No Change
        employeeState.addEmployee(easyTestEmployee2);
        employeeState.addEmployee(easyTestEmployee1);
        employeeState.addEmployee(easyTestEmployee3);
        employeeState.setCurrentDate(LocalDate.parse("2020-04-05"));//not an important date
        employeeState.updateEmployees();
        for (Employee employee : employeeState.getSetOfEmployees()) {
            assertEquals(0,employee.getHolidayLeft());
        }
    }

    @Test
    public void testUpdateEmployeesCase2() { //Holiday accrual
        employeeState.addEmployee(easyTestEmployee2);
        employeeState.addEmployee(easyTestEmployee1);
        employeeState.addEmployee(easyTestEmployee3);
        employeeState.setCurrentDate(LocalDate.parse("2002-01-01"));
        employeeState.updateEmployees();
        assertEquals(easyTestEmployee1.getHolidayAccrual(),easyTestEmployee1.getHolidayLeft());
        assertEquals(0,easyTestEmployee2.getHolidayLeft());
        assertEquals(0,easyTestEmployee3.getHolidayLeft());
    }

    @Test
    public void testUpdateEmployeesCase3() { //Sick Leave accrual
        easyTestEmployee1.setAnniversary("2002-05-06");
        employeeState.addEmployee(easyTestEmployee2);
        employeeState.addEmployee(easyTestEmployee1);
        employeeState.addEmployee(easyTestEmployee3);
        employeeState.setCurrentDate(LocalDate.parse("2019-12-31"));
        employeeState.incrementDate();
        for (Employee employee : employeeState.getSetOfEmployees()) {
            assertEquals(6,employee.getSickLeaveLeft());
        }
    }

    @Test
    public void testUpdateEmployeesCase4() { //Sick leave and holdiay accrual
        employeeState.addEmployee(easyTestEmployee2);
        employeeState.addEmployee(easyTestEmployee1);
        employeeState.addEmployee(easyTestEmployee3);
        employeeState.setCurrentDate(LocalDate.parse("2019-12-31"));
        employeeState.incrementDate();
        assertEquals(easyTestEmployee1.getHolidayAccrual(),easyTestEmployee1.getHolidayLeft());
        assertEquals(6,easyTestEmployee1.getSickLeaveLeft());
    }

    @Test
    public void testUpdateEmployeesCase5() { // Jacoco coverage for UpdateEmployees -01-01 sick leave accrual
        testEmployee1.setAnniversary("2020-03-04");
        employeeState.addEmployee(testEmployee1);
        employeeState.setCurrentDate(LocalDate.parse("2019-01-01"));
        employeeState.updateEmployees();

        assertEquals(6,testEmployee1.getSickLeaveLeft());
    }

    @Test
    public void testUpdateEmployeesCase6() { // Jacoco coverage for UpdateEmplyees anniversary
        testEmployee1.setAnniversary("2020-03-04");
        employeeState.addEmployee(testEmployee1);
        employeeState.setCurrentDate(LocalDate.parse("2019-03-04"));
        employeeState.updateEmployees();

        assertEquals(testEmployee1.getHolidayAccrual(),testEmployee1.getHolidayLeft());
    }

    @Test
    public void testUpdateEmployeesCase7() { // Jacoco coverage for UpdateEmployees -01-nn sick leave accrual
        testEmployee1.setAnniversary("2020-03-04");
        employeeState.addEmployee(testEmployee1);
        testEmployee1.setSickLeaveLeft(0);
        employeeState.setCurrentDate(LocalDate.parse("2019-01-18"));
        employeeState.updateEmployees();

        assertEquals(0,testEmployee1.getSickLeaveLeft());
    }

    @Test
    public void testUpdateEmployeesCase8() { // Jacoco coverage for UpdateEmployees -nn-01 sick leave accrual
        testEmployee1.setAnniversary("2020-03-04");
        employeeState.addEmployee(testEmployee1);
        testEmployee1.setSickLeaveLeft(0);
        employeeState.setCurrentDate(LocalDate.parse("2019-07-01"));
        employeeState.updateEmployees();

        assertEquals(0,testEmployee1.getSickLeaveLeft());
    }

    @Test
    public void testUpdateEmployeesCase9() { // Jacoco coverage for UpdateEmployees -nn-nn sick leave accrual
        testEmployee1.setAnniversary("2020-03-04");
        employeeState.addEmployee(testEmployee1);
        testEmployee1.setSickLeaveLeft(0);
        employeeState.setCurrentDate(LocalDate.parse("2019-07-18"));
        employeeState.updateEmployees();

        assertEquals(0,testEmployee1.getSickLeaveLeft());
    }

    @Test
    public void testUpdateEmployeesCase10() { // Jacoco coverage for UpdateEmployees Anniversary Holiday accrual
        testEmployee1.setAnniversary("2020-03-04");
        employeeState.addEmployee(testEmployee1);
        testEmployee1.setHolidayLeft(0);
        employeeState.setCurrentDate(LocalDate.parse("2019-03-04"));
        employeeState.updateEmployees();

        assertEquals(testEmployee1.getHolidayAccrual(),testEmployee1.getHolidayLeft());
    }

    @Test
    public void testUpdateEmployeesCase11() { // Jacoco coverage for UpdateEmployees Anniversary Holiday accrual
        testEmployee1.setAnniversary("2020-03-04");
        employeeState.addEmployee(testEmployee1);
        testEmployee1.setHolidayLeft(0);
        employeeState.setCurrentDate(LocalDate.parse("2019-04-04"));
        employeeState.updateEmployees();

        assertEquals(0,testEmployee1.getHolidayLeft());
    }

    @Test
    public void testUpdateEmployeesCase12() { // Jacoco coverage for UpdateEmployees Anniversary Holiday accrual
        testEmployee1.setAnniversary("2020-03-04");
        employeeState.addEmployee(testEmployee1);
        testEmployee1.setHolidayLeft(0);
        employeeState.setCurrentDate(LocalDate.parse("2019-03-05"));
        employeeState.updateEmployees();

        assertEquals(0,testEmployee1.getHolidayLeft());
    }

    @Test
    public void testUpdateEmployeesCase13() { // Jacoco coverage for UpdateEmployees Anniversary Holiday accrual
        testEmployee1.setAnniversary("2020-03-04");
        employeeState.addEmployee(testEmployee1);
        testEmployee1.setHolidayLeft(0);
        employeeState.setCurrentDate(LocalDate.parse("2019-04-05"));
        employeeState.updateEmployees();

        assertEquals(0,testEmployee1.getHolidayLeft());
    }

    @Test
    void testUpdateDate() {
        employeeState.addEmployee(testEmployee1);
        employeeState.update(LocalDate.parse("2022-03-03"));
        assertEquals(LocalDate.parse("2022-03-03"),employeeState.getCurrentDate());
        assertEquals(19,testEmployee1.getYearsOfService());
    }

    @Test
    public void testIncrementDate() {
        employeeState.incrementDate();
        assertEquals(LocalDate.parse("2010-01-02"),employeeState.getCurrentDate());
    }

    @Test
    void testSearchEmployee_Null() {
        try {
            employeeState.searchEmployees("Roland Radish, Regal Rennaisance Ripley");
            fail("Employee should not exist");
        } catch (EmployeeNotFoundException e) {
            //pass
        }
    }

    @Test
    void testSearchEmployee_Found() {
        employeeState.addEmployee(testEmployee1);
        try {
            employeeState.searchEmployees("Jerry");
        } catch (EmployeeNotFoundException e) {
            fail("Jerry should be there");
        }
    }

    @Test
    void testSearchEmployee_NotFound() {
        employeeState.addEmployee(testEmployee1);
        try {
            employeeState.searchEmployees("Harold");
            fail("Harold, barely even know her (employee should not exist");
        } catch (EmployeeNotFoundException e) {
            // pass
        }
    }
}
