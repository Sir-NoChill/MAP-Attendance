package tests;

import model.leave.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDate;

public class LeaveTest {

    public Holiday holiday1;
    public Holiday holiday2;
    public Sick sick1;
    public Sick sick2;

    @BeforeEach
    public void setup() {
        holiday1 = new Holiday(LocalDate.parse("2023-12-02"), "Dildo, NL, Canada");
        holiday2 = new Holiday(LocalDate.now(),"Obama Junior High School, nagasaki, Japan");

        sick1 = new Sick(LocalDate.parse("2022-04-02"),"Covid-19");
        sick2 = new Sick(LocalDate.parse("2021-08-11"),"Hit by lightning thrice, went to hospital");
    }

    @Test
    public void testConstructor() {
        assertEquals(LocalDate.parse("2023-12-02"),holiday1.getDateOfLeave());
        assertEquals(LocalDate.now(),holiday2.getDateOfLeave());
        assertEquals(LocalDate.parse("2022-04-02"),sick1.getDateOfLeave());
        assertEquals(LocalDate.parse("2021-08-11"),sick2.getDateOfLeave());

        assertEquals("Dildo, NL, Canada",holiday1.getComments());
        assertEquals("Obama Junior High School, nagasaki, Japan",holiday2.getComments());
        assertEquals("Covid-19",sick1.getComments());
        assertEquals("Hit by lightning thrice, went to hospital",sick2.getComments());
    }

    @Test
    public void testDisplayLeave() {
        assertEquals("2023-12-02" + " " + "Dildo, NL, Canada", holiday1.displayLeave());
        assertEquals(LocalDate.now().toString() + " " + "Obama Junior High School, nagasaki, Japan",
                holiday2.displayLeave());
        assertEquals("2022-04-02" + " " + "Covid-19", sick1.displayLeave());
        assertEquals("2021-08-11" + " " + "Hit by lightning thrice, went to hospital",
                sick2.displayLeave());
    }
}
