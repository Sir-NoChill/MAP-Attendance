package model;

import exceptions.InvalidLeaveAmountException;
import exceptions.LeaveNotFoundException;
import exceptions.RoleNotFoundException;
import exceptions.WorkHoursNotFoundException;
import model.leave.Leave;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static model.Employee.stringToRole;
import static model.Employee.stringToWorkHours;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.Period;

import static model.Role.*;
import static model.leave.LeaveType.*;
import static model.WorkHours.*;

class EmployeeTest {
    Employee testEmployee1;
    Employee testEmployee2;
    Employee testEmployee3;
    Employee testEmployee4;

    //TODO define the constructions of the employees
    @BeforeEach
    public void beforeEach() {
        testEmployee1 = new Employee("2002-07-08", LEGAL_ASSISTANT, "Jerry", SIX_HALF, "Jane", "Criminal Law");
        testEmployee2 = new Employee(LocalDate.now(), HUMAN_RESOURCES, "Harold", SEVEN_HALF, "Kane", "Realty");
        testEmployee3 = new Employee("2015-08-07",ACCOUNTANT,"Merry",SEVEN,"Bane","Wills");
        testEmployee4 = new Employee(LocalDate.of(2017,5,13),ACCOUNTANT,"Carol",SIX_HALF,"Vane","Bert");
    }

    @Test //TODO check the rest of the test employee constructions
    public void testEmployeeConstructorTestCase1() {
        assertEquals("Jerry", testEmployee1.getName());
        assertEquals(LEGAL_ASSISTANT, testEmployee1.getRole());
        assertEquals(LocalDate.parse("2002-07-08"),
                testEmployee1.getAnniversary());
        assertEquals(Period.between(testEmployee1.getAnniversary(),LocalDate.now()).getYears(),
                testEmployee1.getYearsOfService());
        assertEquals(SIX_HALF,testEmployee1.getWorkHours());
        assertTrue(testEmployee1.getLeaveTaken().isEmpty());
        assertEquals("Jane",testEmployee1.getSupervisor());
        assertEquals("Criminal Law",testEmployee1.getDepartment());
        assertEquals(23,testEmployee1.getHolidayAccrual());
    }

    @Test
    public void testEmployeeConstructorTestCase2() {
        assertEquals("Harold", testEmployee2.getName());
        assertEquals(HUMAN_RESOURCES, testEmployee2.getRole());
        assertEquals(LocalDate.now(),
                testEmployee2.getAnniversary());
        assertEquals(Period.between(testEmployee2.getAnniversary(),LocalDate.now()).getYears(),
                testEmployee2.getYearsOfService());
        assertEquals(SEVEN_HALF,testEmployee2.getWorkHours());
        assertTrue(testEmployee2.getLeaveTaken().isEmpty());
        assertEquals("Kane",testEmployee2.getSupervisor());
        assertEquals("Realty",testEmployee2.getDepartment());
        assertEquals(0,testEmployee2.getHolidayAccrual());
    }

    @Test
    public void testEmployeeConstructorTestCase3() {
        assertEquals("Merry", testEmployee3.getName());
        assertEquals(ACCOUNTANT, testEmployee3.getRole());
        assertEquals(LocalDate.parse("2015-08-07"),
                testEmployee3.getAnniversary());
        assertEquals(Period.between(testEmployee3.getAnniversary(),LocalDate.now()).getYears(),
                testEmployee3.getYearsOfService());
        assertEquals(SEVEN,testEmployee3.getWorkHours());
        assertTrue(testEmployee3.getLeaveTaken().isEmpty());
        assertEquals("Bane",testEmployee3.getSupervisor());
        assertEquals("Wills",testEmployee3.getDepartment());
    }

    @Test
    public void testSetAnniversaryString() {
        testEmployee1.setAnniversary("2020-01-01");
        assertEquals(LocalDate.parse("2020-01-01"),testEmployee1.getAnniversary());
    }

    @Test
    public void testSetRole() {
        testEmployee1.setRole(HUMAN_RESOURCES);
        assertEquals(HUMAN_RESOURCES,testEmployee1.getRole());
    }

    @Test
    public void testSetName() {
        testEmployee1.setName("Harry");
        assertEquals("Harry",testEmployee1.getName());
    }

    @Test
    public void testSetWorkHours() {
        testEmployee1.setWorkHours(SIX_HALF);
        assertEquals(SIX_HALF,testEmployee1.getWorkHours());
    }

    @Test
    public void testSetSupervisor() {
        testEmployee1.setSupervisor("Carol");
        assertEquals("Carol",testEmployee1.getSupervisor());
    }

    @Test
    public void testSetDepartment() {
        testEmployee1.setDepartment("Psych");
        assertEquals("Psych",testEmployee1.getDepartment());
    }

    @Test
    public void testTakeLeaveCase1() {
        testEmployee1.setHolidayLeft(3);
        testEmployee1.setSickLeaveLeft(2);

        try {
            testEmployee1.takeLeave("2022-08-16", SICK, "Covid-19");
        } catch (InvalidLeaveAmountException e) {
            fail("no exception expected");
        }
        assertEquals(1, testEmployee1.getLeaveTaken().size());
        assertEquals(1,testEmployee1.getSickLeaveLeft());

        try {
            testEmployee1.takeLeave(LocalDate.now(), HOLIDAY, "keep your cardiologist's number on hand");
        } catch (InvalidLeaveAmountException e) {
            fail("no exception expected");
        }
        assertEquals(2,testEmployee1.getLeaveTaken().size());
        assertEquals(2,testEmployee1.getHolidayLeft());
    }

    @Test
    public void testTakeLeaveCase2() {
        testEmployee1.setHolidayLeft(4);

        try {
            testEmployee1.takeLeave("2020-07-08", HOLIDAY, "Off to commit arson");
        } catch (InvalidLeaveAmountException e) {
            fail("no exception expected");
        }
        assertEquals(3,testEmployee1.getHolidayLeft());
        try {
            testEmployee1.takeLeave("2020-07-09",HOLIDAY,"Commited some arson, now in jail");
        } catch (InvalidLeaveAmountException e) {
            fail("no exception expected");
        }
        assertEquals(2,testEmployee1.getHolidayLeft());
    }

    @Test
    public void testTakeLeaveCase3() {
        testEmployee1.setSickLeaveLeft(4);

        try {
            testEmployee1.takeLeave(LocalDate.now(),SICK,"Ash in lungs from arson");
        } catch (InvalidLeaveAmountException e) {
            fail("no exception expected");
        }
        assertEquals(3,testEmployee1.getSickLeaveLeft());
        try {
            testEmployee1.takeLeave("2007-04-03",SICK,"Got stabbed");
        } catch (InvalidLeaveAmountException e) {
            fail("no exception expected");
        }
        assertEquals(2,testEmployee1.getSickLeaveLeft());
    }

    @Test
    void testTakeLeaveCase4_invalidLeaveAmount() {
        testEmployee1.setHolidayLeft(0);
        testEmployee1.setSickLeaveLeft(0);

        try {
            testEmployee1.takeLeave("2020-01-01",SICK,"Impaled");
            fail("InvalidLeaveAmountExcetption expected");
        } catch (InvalidLeaveAmountException e) {
            //pass
        }
    }

    @Test
    public void testAccrueVacation() {
        testEmployee1.setHolidayAccrual(6);
        testEmployee1.accrueHoliday();
        assertEquals(6,testEmployee1.getHolidayLeft());

        testEmployee1.setHolidayAccrual(3);
        testEmployee1.accrueHoliday();
        assertEquals(9,testEmployee1.getHolidayLeft());
    }

    @Test
    public void testAccrueSickDays() {
        testEmployee1.accrueSickDays();
        assertEquals(6,testEmployee1.getSickLeaveLeft());

        testEmployee1.accrueSickDays();
        assertEquals(6,testEmployee1.getSickLeaveLeft());
    }

    @Test
    public void setHolidayAccrualRateCase1() {
        testEmployee1.setAnniversary(LocalDate.now());
        testEmployee1.autoSetYearsOfService();
        testEmployee1.autoSetHolidayAccrualRate();
        assertEquals(0,testEmployee1.getYearsOfService());
        assertEquals(0,testEmployee1.getHolidayAccrual());
    }

    @Test
    public void setHolidayAccrualRateCase2() {
        testEmployee1.setAnniversary(LocalDate.now().minusYears(1));
        testEmployee1.autoSetYearsOfService();
        testEmployee1.autoSetHolidayAccrualRate();
        assertEquals(1,testEmployee1.getYearsOfService());
        assertEquals(10,testEmployee1.getHolidayAccrual());
    }

    @Test
    public void setHolidayAccrualRateCase3() {
        testEmployee1.setAnniversary(LocalDate.now().minusYears(2));
        testEmployee1.autoSetYearsOfService();
        testEmployee1.autoSetHolidayAccrualRate();
        assertEquals(2,testEmployee1.getYearsOfService());
        assertEquals(15,testEmployee1.getHolidayAccrual());
    }

    @Test
    public void setHolidayAccrualRateCase4() {
        testEmployee1.setAnniversary(LocalDate.now().minusYears(3));
        testEmployee1.autoSetYearsOfService();
        testEmployee1.autoSetHolidayAccrualRate();
        assertEquals(3,testEmployee1.getYearsOfService());
        assertEquals(15,testEmployee1.getHolidayAccrual());
    }

    @Test
    public void setHolidayAccrualRateCase5() {
        testEmployee1.setAnniversary(LocalDate.now().minusYears(4));
        testEmployee1.autoSetYearsOfService();
        testEmployee1.autoSetHolidayAccrualRate();
        assertEquals(4,testEmployee1.getYearsOfService());
        assertEquals(15,testEmployee1.getHolidayAccrual());
    }

    @Test
    public void setHolidayAccrualRateCase6() {
        testEmployee1.setAnniversary(LocalDate.now().minusYears(5));
        testEmployee1.autoSetYearsOfService();
        testEmployee1.autoSetHolidayAccrualRate();
        assertEquals(5,testEmployee1.getYearsOfService());
        assertEquals(16,testEmployee1.getHolidayAccrual());
    }

    @Test
    public void setHolidayAccrualRateCase7() {
        testEmployee1.setAnniversary(LocalDate.now().minusYears(6));
        testEmployee1.autoSetYearsOfService();
        testEmployee1.autoSetHolidayAccrualRate();
        assertEquals(6,testEmployee1.getYearsOfService());
        assertEquals(17,testEmployee1.getHolidayAccrual());
    }

    @Test
    public void setHolidayAccrualRateCase8() {
        testEmployee1.setAnniversary(LocalDate.now().minusYears(7));
        testEmployee1.autoSetYearsOfService();
        testEmployee1.autoSetHolidayAccrualRate();
        assertEquals(7,testEmployee1.getYearsOfService());
        assertEquals(17,testEmployee1.getHolidayAccrual());
    }

    @Test
    public void setHolidayAccrualRateCase9() {
        testEmployee1.setAnniversary(LocalDate.now().minusYears(8));
        testEmployee1.autoSetYearsOfService();
        testEmployee1.autoSetHolidayAccrualRate();
        assertEquals(8,testEmployee1.getYearsOfService());
        assertEquals(20,testEmployee1.getHolidayAccrual());
    }

    @Test
    public void setHolidayAccrualRateCase10() {
        testEmployee1.setAnniversary(LocalDate.now().minusYears(9));
        testEmployee1.autoSetYearsOfService();
        testEmployee1.autoSetHolidayAccrualRate();
        assertEquals(9,testEmployee1.getYearsOfService());
        assertEquals(21,testEmployee1.getHolidayAccrual());
    }

    @Test
    public void setHolidayAccrualRateCase11() {
        testEmployee1.setAnniversary(LocalDate.now().minusYears(10));
        testEmployee1.autoSetYearsOfService();
        testEmployee1.autoSetHolidayAccrualRate();
        assertEquals(10,testEmployee1.getYearsOfService());
        assertEquals(22,testEmployee1.getHolidayAccrual());
    }

    @Test
    public void setHolidayAccrualRateCaseDefault() {
        testEmployee1.setAnniversary(LocalDate.now().minusYears(11));
        testEmployee1.autoSetYearsOfService();
        testEmployee1.autoSetHolidayAccrualRate();
        assertEquals(11,testEmployee1.getYearsOfService());
        assertEquals(23,testEmployee1.getHolidayAccrual());
    }

    @Test
    public void testSearchLeaveCase1() throws InvalidLeaveAmountException {
        testEmployee1.setSickLeaveLeft(50);
        testEmployee1.setHolidayLeft(50);

        testEmployee1.takeLeave("2020-07-08", HOLIDAY, "Off to commit arson");
        testEmployee1.takeLeave("2020-07-09",HOLIDAY,"Commited some arson, now in jail");

        testEmployee1.takeLeave(LocalDate.now(),SICK,"Ash in lungs from arson");
        testEmployee1.takeLeave("2007-04-03",SICK,"Got stabbed");

        Leave searchResult;
        try {
            searchResult = testEmployee1.searchLeave("2020-07-08");
        } catch (LeaveNotFoundException e) {
            fail("no exception expected");
        }
    }

    @Test
    public void testSearchLeaveCase2() {
        Leave searchResult;
        try {
            searchResult = testEmployee2.searchLeave("2020-07-08");
        } catch (LeaveNotFoundException e) {
            //pass
        }
    }

    @Test
    void testStringToRole_Accountant() {
        try {
            assertEquals(ACCOUNTANT,stringToRole("accountant"));
        } catch (RoleNotFoundException e) {
            fail("no exception expected");
        }
    }

    @Test
    void testStringToRole_HR() {
        try {
            assertEquals(HUMAN_RESOURCES,stringToRole("human_resources"));
            assertEquals(HUMAN_RESOURCES,stringToRole("hr"));
        } catch (RoleNotFoundException e) {
            fail("no exception expected");
        }
    }

    @Test
    void testStringToRole_LegalAssistant() {
        try {
            assertEquals(LEGAL_ASSISTANT,stringToRole("legal_assistant"));
        } catch (RoleNotFoundException e) {
            fail("RoleNotFoundException expected");
        }
    }

    @Test
    void testStringToRole_null() {
        try {
            assertNull(stringToRole("silly goose"));
            fail("RoleNotFoundException expected");
        } catch (RoleNotFoundException e) {
            //pass
        }
    }

    @Test
    void testStringToWorkHours_SIX_HALF() {
        try {
            assertEquals(SIX_HALF,stringToWorkHours("six_half"));
            assertEquals(SIX_HALF,stringToWorkHours("6.5"));
        } catch (WorkHoursNotFoundException e) {
            fail("No exception expected");
        }
    }

    @Test
    void testStringToWorkHours_SEVEN() {
        try {
            assertEquals(SEVEN,stringToWorkHours("seven"));
            assertEquals(SEVEN,stringToWorkHours("7"));
        } catch (WorkHoursNotFoundException e) {
            fail("No exception expected");
        }
    }

    @Test
    void testStringToWorkHours_SEVEN_HALF() {
        try {
            assertEquals(SEVEN_HALF,stringToWorkHours("7.5"));
            assertEquals(SEVEN_HALF,stringToWorkHours("seven_half"));
        } catch (WorkHoursNotFoundException e) {
            fail("No exception expected");
        }
    }

    @Test
    void testStringToWorkHours_Null() {
        try {
            assertNull(stringToWorkHours("eeee"));
            fail("WorkHoursNotFoundException expected");
        } catch (WorkHoursNotFoundException e) {
            //pass
        }
    }
}