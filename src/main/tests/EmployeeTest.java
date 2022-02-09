package tests;

import model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    public void testTakeLeave() {
        testEmployee1.setHolidayLeft(3);
        testEmployee1.setSickLeaveLeft(2);

        testEmployee1.takeLeave("2022-08-16", SICK, "Covid-19");
        assertEquals(1, testEmployee1.getLeaveTaken().size());
        assertEquals(2,testEmployee1.getHolidayLeft());

        testEmployee1.takeLeave(LocalDate.now(), HOLIDAY, "keep your cardiologist's number on hand");
        assertEquals(2,testEmployee1.getLeaveTaken().size());
        assertEquals(1,testEmployee1.getHolidayLeft());
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
}